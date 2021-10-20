import java.util.LinkedList;

public class QueueDirectory {
	private LinkedList<String> sub_dir;
	
	public QueueDirectory() {
		this.sub_dir = new LinkedList<>();
	}
	
	public void putDirectory(String new_dir){
		synchronized(this) {
			sub_dir.add(new_dir);
			notify();
		}
		
	} 
	
	public synchronized String takeDirectory() {
		while(sub_dir.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		String name_dir = sub_dir.poll();
		if(name_dir == null) {
			putDirectory(null);
		}
		
		return name_dir;
	}
	
	public void print_list() {
		for(String s : sub_dir) {
			System.out.println(s + "\n");
		}
	}
}
