package BankManager;

import java.util.*;

public class ContoCorrente {
	private String nomeCorrentista;
	private String cognomeCorrentista;
	private LinkedList<Movimento> movimentiCorrentista;
	
	public ContoCorrente(String correntista, String cognome) {
		this.nomeCorrentista = correntista;
		this.cognomeCorrentista = cognome;
		this.movimentiCorrentista = new LinkedList<>();
	}
	
	public void addMovimento(Movimento m) {
		movimentiCorrentista.add(m);
	}
	
	public LinkedList<Movimento> getMovimenti(){
		return movimentiCorrentista;
	}
	
	public String toString() {
		return "Conto corrente di " + nomeCorrentista + " " + cognomeCorrentista + "\n Numero movimenti: " + movimentiCorrentista.size();
	}
	
}
