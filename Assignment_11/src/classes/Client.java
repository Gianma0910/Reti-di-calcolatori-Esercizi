package classes;

import interfaces.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

public class Client extends RemoteObject implements ClientInterface{
	private String nameSpeaker;
	
	public Client(String nameSpeaker) throws RemoteException{
		super();
		this.nameSpeaker = nameSpeaker;
	}

	public void notExistedSession(int numberSession) {
		System.out.println("Impossibilie registrarsi alla sessione " + numberSession + ", non esiste\n");
	}
	
	public void sessionFull(int numberSession) {
		System.out.println("Impossibile registrarsi alla sessione " + numberSession + ", � tutta occupata\n");
	}

	public void alreadyRegisteredInThatSession(int numberSession, String nameSpeaker) {
		System.out.println("Lo speaker " + nameSpeaker + "� gi� stato registrato per la sessione " + numberSession + "\n");
	}
	
	public String getNameSpeaker() throws RemoteException{
		return nameSpeaker;
	}
}