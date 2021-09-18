
public class GregoryLeibniz implements Runnable {
	
	private double accuracy;
	private double pi = 0;
	
	public GregoryLeibniz(double accuracy) {
		this.accuracy = accuracy;
	}
	
	@Override
	public void run() {
		int i = 0;
		
		while(!Thread.currentThread().isInterrupted() && Math.abs(pi-Math.PI) >= accuracy) {
			pi += (4*Math.pow(-1, i))/(2*i+1); 
			i++;
		}

	}
	
	double getPI() {
		return pi;
	}

}
