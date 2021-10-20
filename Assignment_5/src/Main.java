import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		if(args.length != 3) {
			System.err.println("Numero di parametri inseriti errato");
			System.err.println("Inserire: percorso assoluto directory; numero di thread consumatori; nome file dove scrivere i contenuti della directory");
		
			return;
		}
		
		File directory = new File(args[0]);
		int num_consumer = Integer.parseInt(args[1]);
		File file_output = new File(args[2]);
		
		QueueDirectory queue = new QueueDirectory();
		Printer printer = new Printer(file_output);
		LinkedList<Thread> consumers = new LinkedList<>();
		
		Thread producer = new Thread(new Producer(directory, queue));
		
		for(int i = 0; i < num_consumer; i++) {
			Thread consumer = new Thread(new Consumer(queue, printer));
			consumers.add(consumer);
			consumer.start();
		}
	
		Thread.sleep(1000);
		producer.start();
		
		producer.join();
		
		for(Thread t : consumers) {
			t.join();
		}
		
		printer.close();
	}
}
