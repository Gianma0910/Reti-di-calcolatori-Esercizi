import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TutorLaboratorio {
	private ArrayList<Boolean> computers;
	private ReentrantLock lock = new ReentrantLock();
	Condition freeComputer;
	Condition profCondition;
	
	public TutorLaboratorio() {
		computers = new ArrayList<>(Arrays.asList(new Boolean[20]));
		Collections.fill(computers, Boolean.FALSE);
		this.freeComputer = lock.newCondition();
		this.profCondition = lock.newCondition();
	}
	
	public void getComputer(int index_computer, String message) throws InterruptedException {
		try {
			lock.lock();
			while(computers.get(index_computer).equals(Boolean.TRUE) || lock.hasWaiters(profCondition)){
				if(lock.hasWaiters(profCondition)) {
					profCondition.signal();
				}
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
				profCondition.await();
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
			while(!computers.contains(Boolean.FALSE) || lock.hasWaiters(profCondition)){
				if(lock.hasWaiters(profCondition)) {
					profCondition.signal();
				}
				freeComputer.await();
			}
			
			int index = computers.indexOf(Boolean.FALSE);
			computers.set(index, Boolean.TRUE);
			lock.unlock();
			
			System.out.println(message);
			Thread.sleep(3000);
			
			lock.lock();
			computers.set(index, Boolean.FALSE);
			freeComputer.signal();
		}finally {
			lock.unlock();
		}
	}
}
