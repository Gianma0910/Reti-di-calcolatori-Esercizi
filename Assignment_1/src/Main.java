
public class Main {

	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.out.println("Numero di argomenti passati errato");
			return;
		}
		
		double accuracy;
		int time;
		
		try {
			time = Integer.parseInt(args[1]);
			accuracy = Double.parseDouble(args[0]);
		}catch (Exception e) {
			System.out.println("Argomenti passati non validi");
			return;
		}

		GregoryLeibniz gl = new GregoryLeibniz(accuracy);
		
		Thread t = new Thread(gl);
		
		t.start();
		
		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		t.interrupt();
		
		System.out.println("PI calcolato: "+gl.getPI());

	}

}
