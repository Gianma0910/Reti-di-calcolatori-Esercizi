package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	
	public void notExistedSession(int numberSession) throws RemoteException;
	public void sessionFull(int numberSession) throws RemoteException;
	public void alreadyRegisteredInThatSession(int numberSession, String name) throws RemoteException;
	public String getNameSpeaker() throws RemoteException;
}