import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UfficioPostale {
	
	public static final int num_sportelli = 4;
	
	public static void main(String[] args) {
		if(args.length < 2) {
			System.err.println("Numero di argomenti passati errato\n");
			System.err.println("Passare il numero di k persone che possono entrare nella seconda sala e il numero di persone da servire\n");
			return;
		}
		int k_persone = Integer.parseInt(args[0]);
		int tot_persone = Integer.parseInt(args[1]);
		int chiusura_sportello = ThreadLocalRandom.current().nextInt() % 1000;
		ArrayBlockingQueue<Runnable> seconda_sala = new ArrayBlockingQueue<Runnable>(k_persone);
		ThreadPoolExecutor ufficio = new ThreadPoolExecutor(num_sportelli, num_sportelli, Math.abs(chiusura_sportello), TimeUnit.MILLISECONDS, seconda_sala);
		LinkedList<Runnable> sala_attesa = new LinkedList<Runnable>();
		
		ufficio.allowCoreThreadTimeOut(true);

		for(int id_persona = 0; id_persona < tot_persone; id_persona++) {
			Persone p = new Persone(id_persona);
			if(seconda_sala.remainingCapacity() != 0 && !sala_attesa.isEmpty()) {
				ufficio.execute(sala_attesa.removeFirst());
			}else {
				try {
					ufficio.execute(p);
				}catch(RejectedExecutionException e) {
					System.out.println("Sportelli e seconda sala pieni, persona " + p.getIdPersona() + " vada in sala d'attesa\n");
					sala_attesa.add(p);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ufficio.shutdown();
		try {
			if(!ufficio.awaitTermination(chiusura_sportello, TimeUnit.MILLISECONDS)) {
				ufficio.shutdownNow();
			}
		}catch(Exception e) {
			ufficio.shutdownNow();
		}
		
	}
}
