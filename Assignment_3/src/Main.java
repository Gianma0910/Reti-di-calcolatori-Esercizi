import java.util.ArrayList;
import java.util.Collections;

public class Main {
	public static void main(String[] args) {
		if(args.length != 3) {
			System.err.println("Numero di parametri passati errato\n");
			System.err.println("Inserire numero di professori, tesisti e studenti che useranno il laboratorio\n");
			return;
		}
		
		try {
			int num_professori = Integer.parseInt(args[0]);
			int num_tesisti = Integer.parseInt(args[1]);
			int num_studenti = Integer.parseInt(args[2]);
			
			TutorLaboratorio lab = new TutorLaboratorio();
			
			ArrayList<Utente> utenti_laboratorio = new ArrayList<>(num_professori + num_tesisti + num_studenti);
			
			for(int i = 0; i < num_professori; i++) {
				Utente professore = new Utente(TYPE_USERS.PROFESSORE, lab);
				utenti_laboratorio.add(professore);
			}
			
			for(int i = 0; i < num_tesisti; i++) {
				Utente tesista = new Utente(TYPE_USERS.TESISTA, lab);
				utenti_laboratorio.add(tesista);
			}
			
			for(int i = 0; i < num_studenti; i++) {
				Utente studente = new Utente(TYPE_USERS.STUDENTE, lab);
				utenti_laboratorio.add(studente);
			}
			
			Collections.shuffle(utenti_laboratorio);

			for(Utente u : utenti_laboratorio) {
				u.start();
			}
			
			for(Utente u : utenti_laboratorio) {
				u.join();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
