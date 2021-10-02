import java.util.concurrent.ThreadLocalRandom;

public class Persone implements Runnable {
	private int id_persona;
	
	public Persone(int id_persona) {
		this.id_persona = id_persona;
	}
	
	public int getIdPersona() {
		return id_persona;
	}
	
	@Override
	public void run() {
		System.out.println("Persona " + id_persona + ": sta eseguendo la sua operazione\n");
		int tempo_operazione = ThreadLocalRandom.current().nextInt() % 10000;
		try {
			Thread.sleep(Math.abs(tempo_operazione));
		}catch(InterruptedException e) {
			System.out.println("La persona " + id_persona + " non ha finito in tempo la sua operazione\n");
			return;
		}
		System.out.println("Persona " + id_persona + ": ha concluso la sua operazione\n");
	}
}
