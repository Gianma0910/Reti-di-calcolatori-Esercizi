package JSONManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import BankManager.ContoCorrente;

public class JSONFileCreator {
	private ArrayList<ContoCorrente> contiCorrenti;
	private final String PATH = "src\\ContiCorrenti.json";
	
	public JSONFileCreator(ArrayList<ContoCorrente> conti) {
		this.contiCorrenti = conti;
	}
	
	public void create() {
		if(contiCorrenti.isEmpty())
			return;
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		StringBuilder result = new StringBuilder();
		
		result.append(gson.toJson(contiCorrenti));
		
		try {
			writeToFile(result.toString());
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeToFile(String result) throws IOException{
		FileOutputStream fos = new FileOutputStream(PATH);
		FileChannel channel = fos.getChannel();
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(result.length());
		buffer.put(result.getBytes());
		
		buffer.flip();
		channel.write(buffer);
		
		fos.close();
	}
}
