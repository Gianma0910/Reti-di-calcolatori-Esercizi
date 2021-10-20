import java.io.*;

public class Consumer implements Runnable {
	private QueueDirectory queue;
	private Printer printer;
	
	public Consumer(QueueDirectory queue, Printer printer) {
		this.queue = queue;
		this.printer = printer;
	}
	
	@Override
	public void run() {
		String name_dir;
		
		do {
			name_dir = queue.takeDirectory();
			if(name_dir != null) {
				try {
					printer.write_into_file(name_dir);
				} catch (IOException e) {
					name_dir = null;
					System.err.println("Impossibile scrivere il nome della directory");
				}
			}
		}while(name_dir != null);
	}

}
