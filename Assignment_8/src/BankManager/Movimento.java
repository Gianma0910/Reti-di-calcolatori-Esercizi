package BankManager;

import java.util.*;

public class Movimento {
	private String data;
	private Causale causale;
	
	public Movimento(String data, Causale causale) {
		this.data = data;
		this.causale = causale;
	}
	
	public static Movimento newMovimento(Causale c) {
		return new Movimento(new Date().toString(), c);
	}
	
	public Causale getCausale() {
		return causale;
	}
	
	public String toString() {
		return "Causale: " + causale + ", svolto in data: " + data;
	}
}
