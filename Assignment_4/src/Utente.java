import java.util.concurrent.ThreadLocalRandom;

public class Utente extends Thread{
	private TYPE_USERS type;
	private TutorLaboratorio lab;
	private int i = -1;
	private int k_accessi;
	
	public Utente(TYPE_USERS type, TutorLaboratorio lab){
		this.type = type;
		this.lab = lab;
		
		this.k_accessi = ThreadLocalRandom.current().nextInt(10);
		
		if(type == TYPE_USERS.TESISTA)
			i = ThreadLocalRandom.current().nextInt(20);	
	}

	public void run() {
		while(k_accessi > 0) {
			try {
				switch (type) {
					case TESISTA : {
						String message = "TESISTA sta usando il computer" + i + " -> " + Thread.currentThread().getName() + ": accesso numero " + k_accessi; 
						lab.getComputer(i, message);
					}
					case STUDENTE : {
						lab.getAComputer("STUDENTE sta usando un computer -> " + Thread.currentThread().getName() + ": accesso numero " + k_accessi);
					}
					case PROFESSORE : {
						lab.getAllComputers("PROFESSORE è entrato in laboratorio -> " + Thread.currentThread().getName() + ": accesso numero " + k_accessi);
					}
				}
				
				k_accessi--;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public TYPE_USERS getType() {
		return type;
	}
}