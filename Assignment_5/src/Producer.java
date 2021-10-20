import java.io.File;

public class Producer implements Runnable {
	private File root_directory;
	private QueueDirectory queue;
	
	public Producer(File root_directory, QueueDirectory queue) {
		this.root_directory = root_directory;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		if(root_directory == null) {
			queue.putDirectory(null);
			return;
		}
		
		if(!root_directory.isDirectory() || !root_directory.exists()) {
			System.err.println("La directory non esiste oppure non è una directory");
			return;
		}
		
		scanDir(root_directory);
		queue.putDirectory(null);
	}
	
	private void scanDir(File path){
		File [] files = path.listFiles();
		if(files == null) {
			return;
		}
		
		for(File f : files) {
			if(f.isDirectory()) {
				queue.putDirectory(f.getName());
				scanDir(f);
			}else {
				queue.putDirectory(f.getName());
			}
		}
	}

}
