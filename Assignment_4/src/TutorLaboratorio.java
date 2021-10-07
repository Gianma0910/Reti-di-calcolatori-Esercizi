import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class TutorLaboratorio {
	private ArrayList<Boolean> computers;
	private HashSet<Utente> profList;
	
	public TutorLaboratorio(int num_professori) {
		this.computers = new ArrayList<>(Arrays.asList(new Boolean[20]));
		Collections.fill(computers, Boolean.FALSE);
		this.profList = new HashSet<>(num_professori);
	}
	
	public void getComputer(int index_computer, String message) throws InterruptedException {
		synchronized (this) {
			while(computers.get(index_computer).equals(Boolean.TRUE)) {
				wait();
			}
			
			while(!profList.isEmpty()) {
				if(profList.stream().noneMatch(prof -> prof.getState() == Thread.State.BLOCKED)) {
					notify();
				}
				wait();
			}
			computers.set(index_computer, Boolean.TRUE);
		}
		
		System.out.println(message);
		Thread.sleep(3000);
		
		synchronized (this) {
			computers.set(index_computer, Boolean.FALSE);
			notify();
		}
	}
	
	public synchronized void getAllComputers(String message) throws InterruptedException {
		while(computers.contains(Boolean.TRUE)) {
			profList.add((Utente) Thread.currentThread());
			wait();
		}
		
		Collections.fill(computers, Boolean.TRUE);
		profList.remove((Utente) Thread.currentThread());
		
		System.out.println(message);
		Thread.sleep(5000);
			
		Collections.fill(computers, Boolean.FALSE);
		notify();
		
	}
	
	public void getAComputer(String message) throws InterruptedException {
		int index_computer;
		synchronized (this) {
			if(!computers.contains(Boolean.FALSE)) {
				wait();
			}
			
			while(!profList.isEmpty()) {
				if(profList.stream().noneMatch(prof -> prof.getState() == Thread.State.BLOCKED)) {
					notify();
				}
				wait();
			}
			
			index_computer = computers.indexOf(Boolean.FALSE);
			computers.set(index_computer, Boolean.TRUE);
		}
		
		System.out.println(message);
		Thread.sleep(3000);
		
		synchronized (this) {
			computers.set(index_computer, Boolean.FALSE);
			notify();	
		}
	}
}
