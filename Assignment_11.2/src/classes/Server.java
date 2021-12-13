package classes;

import interfaces.ServerInterface;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Server extends RemoteObject implements ServerInterface{
	private Hashtable<Integer, List<String>> speakerSession;

	public Server() throws RemoteException{
		super();
		speakerSession = new Hashtable<>();
		for(int i = 1; i <= 36; i++) {
			speakerSession.put(i, new ArrayList<String>(5));
		}
	}
	
	public synchronized void subscribeToSession(int numberSession, String speaker) throws RemoteException {
		if(numberSession < 1 || numberSession > 36) {
			throw new RemoteException("Sessione " + numberSession + " non esiste\n");
		}
		List<String> list = speakerSession.get(numberSession);
		if(list.size() == 5) {
			throw new RemoteException("Sessione " + numberSession + " tutta occupata\n");
		}
		
		if(list.contains(speaker) == true) {
			throw new RemoteException("Speaker " + speaker + " è già registrato alla sessione " + numberSession + "\n");
		}
		
		list.add(speaker);
		
		System.out.println("Lo speaker " + speaker + " si è registrato per la sessione " + numberSession);
	}
	
	public void printSession(int numberSession) throws RemoteException{
		if(numberSession > 36)
			throw new RemoteException("Impossibile stampare gli speaker della sessione " + numberSession + ", perché non esiste\n");
		
		List<String> list_session = speakerSession.get(numberSession);
		System.out.println("Sessione " + numberSession + ":\t");
		
		for(String ci : list_session) {
			System.out.println(ci + "; ");
		}
		System.out.println("\n");
	}

	public boolean allSessionsFull() throws RemoteException {
		for(int i = 1; i <= 36; i++) {
			List<String> list = speakerSession.get(i);
			if(list.size() == 5) continue;
			else return false;
		}
		
		return true;
	}
}