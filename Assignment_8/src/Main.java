import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import BankManager.Causale;
import BankManager.ContoCorrente;
import BankManager.Movimento;
import JSONManager.JSONFileCreator;
import JSONManager.JSONFileReader;

public class Main {
	
	private static final String[] names = {"Gianmarco", "Dora", "Massimo", "Gabriella", "Giuseppe", "Simone", "Federica", "Jessica", "Gianluca", "Winona"};
	private static final String[] surnames = {"Benedetti", "Petrocchi", "Federighi", "Russo", "Consani", "Ricci", "Bertolucci", "Silla", "Favilla", "Tognini"};
	
	private static final int NUM_ACCOUNTS = 10;
	private static final int NUM_BANK_MOVEMENTS = 200;
	
	public static void main(String[] args) {
		ArrayList<ContoCorrente> conti = new ArrayList<>(NUM_ACCOUNTS);
		Random random = new Random();
		
		for(int i = 0; i < NUM_ACCOUNTS; i++) {
			ContoCorrente cc = new ContoCorrente(names[random.nextInt(names.length)], surnames[random.nextInt(surnames.length)]);
		
			for(int j = 0; j < random.nextInt(NUM_BANK_MOVEMENTS); j++) {
				Causale randomCausale = Causale.values()[random.nextInt(Causale.values().length)];
				cc.addMovimento(Movimento.newMovimento(randomCausale));
			}
			
			conti.add(cc);
		}
		
		JSONFileCreator jFileCreator = new JSONFileCreator(conti);
		jFileCreator.create();
		
		JSONFileReader jFileReader = new JSONFileReader();
		
		try {
			jFileReader.readAndCount();
		}catch(IOException e) {
			System.out.println("Errore nella lettura del file JSON\n");
			return;
		}
		
		String[] result = jFileReader.getResult();
		for(String s : result) {
			System.out.println(s);
		}
	}
}
