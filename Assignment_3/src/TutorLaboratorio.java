import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TutorLaboratorio {
	private ArrayList<Boolean> computers = new ArrayList<>(Arrays.asList(new Boolean[20]));
	private ReentrantLock lock = new ReentrantLock();
	Condition freeComputer;
	
	public TutorLaboratorio() {
		Collections.fill(computers, Boolean.FALSE);
		this.freeComputer = lock.newCondition();
	}
	
	public void getComputer(int index_computer, String message) throws InterruptedException {
		try {
			lock.lock();
			while(computers.get(index_computer).equals(Boolean.TRUE)){
				freeComputer.await();
			}
			
			computers.set(index_computer, Boolean.TRUE);
			lock.unlock();
			System.out.println(message);
			Thread.sleep(3000);
			
			lock.lock();
			computers.set(index_computer, Boolean.FALSE);
			freeComputer.signal();
		}finally {
			lock.unlock();
		}
	}
	
	public void getAllComputers(String message) throws InterruptedException {
		try {
			lock.lock();
			while(computers.contains(Boolean.TRUE)){
				freeComputer.await();
			}
			
			Collections.fill(computers, Boolean.TRUE);
			System.out.println(message);
			Thread.sleep(5000);
			
			Collections.fill(computers, Boolean.FALSE);
			freeComputer.signal();
		}finally {
			lock.unlock();
		}
		
	}
	
	public void getAComputer(String message) throws InterruptedException {
		try {
			lock.lock();
			while(!computers.contains(Boolean.FALSE)){
				freeComputer.await();
			}
			
			int index = computers.indexOf(Boolean.FALSE);
			computers.set(index, Boolean.TRUE);
			lock.unlock();
			
			System.out.println("Computer " + index + message);
			Thread.sleep(3000);
			
			lock.lock();
			computers.set(index, Boolean.FALSE);
			freeComputer.signal();
		}finally {
			lock.unlock();
		}
	}
}
