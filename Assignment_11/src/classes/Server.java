package classes;

import interfaces.ServerInterface;
import interfaces.ClientInterface;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Server extends RemoteObject implements ServerInterface{
	private Hashtable<Integer, List<ClientInterface>> speakerSession;

	public Server() throws RemoteException{
		super();
		speakerSession = new Hashtable<>();
		for(int i = 1; i <= 36; i++) {
			speakerSession.put(i, new ArrayList<ClientInterface>(5));
		}
	}
	
	public synchronized void subscribeToSession(int numberSession, ClientInterface speaker) throws RemoteException {
		if(numberSession < 1 || numberSession > 36) {
			speaker.notExistedSession(numberSession);
			return;
		}
		List<ClientInterface> list = speakerSession.get(numberSession);
		if(list.size() == 5) {
			speaker.sessionFull(numberSession);
			return;
		}
		
		if(list.contains(speaker) == true) {
			speaker.alreadyRegisteredInThatSession(numberSession, speaker.getNameSpeaker());
			return;
		}
		
		list.add(speaker);
		
		System.out.println("Lo speaker " + speaker.getNameSpeaker() + " si è registrato per la sessione " + numberSession);
	}
	
	public void printSession(int numberSession) throws RemoteException{
		List<ClientInterface> list_session = speakerSession.get(numberSession);
		System.out.println("Sessione " + numberSession + ":\t");
		
		for(ClientInterface ci : list_session) {
			System.out.println(ci.getNameSpeaker() + "; ");
		}
		System.out.println("\n");
	}

	public boolean allSessionsFull() throws RemoteException {
		for(int i = 1; i <= 36; i++) {
			List<ClientInterface> list = speakerSession.get(i);
			if(list.size() == 5) continue;
			else return false;
		}
		
		return true;
	}
}