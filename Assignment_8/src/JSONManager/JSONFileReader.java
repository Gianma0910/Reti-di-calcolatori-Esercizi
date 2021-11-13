package JSONManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.google.gson.stream.JsonReader;
import com.google.gson.Gson;
import BankManager.Causale;
import BankManager.ContoCorrente;
import BankManager.Movimento;

public class JSONFileReader {
	private ConcurrentHashMap<Causale, Integer> database;
	
	public JSONFileReader() {
		database = new ConcurrentHashMap<>(Causale.values().length);
		for(Causale c : Causale.values()) {
			database.put(c, 0);
		}
	}
	
	public void readAndCount() throws IOException{
		ExecutorService pool = Executors.newFixedThreadPool(10);
		
		JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream("src\\ContiCorrenti.json")));
		jsonReader.beginArray();
		
		while(jsonReader.hasNext()) {
			ContoCorrente cc = new Gson().fromJson(jsonReader, ContoCorrente.class);
			pool.submit(counterTask(cc));
		}
		
		pool.shutdown();
		
		try {
			boolean end = pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			if(!end) {
				pool.shutdownNow();
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private Runnable counterTask(ContoCorrente cc) {
		return () -> {
			for(Movimento m : cc.getMovimenti()) {
				Causale c = m.getCausale();
				database.put(c, database.get(c) + 1);
			}
		};
	}

	public String[] getResult() {
		String[] result = new String[Causale.values().length];
		int index = 0;
		
		for(Map.Entry<Causale, Integer> entry : database.entrySet()) {
			result[index] = entry.getKey() + " " + entry.getValue();
			index++;
		}
		
		Arrays.sort(result);
		return result;
	}
	
}
