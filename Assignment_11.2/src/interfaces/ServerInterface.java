package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

	public void subscribeToSession(int numberSession, String nameSpeaker) throws RemoteException;
	public void printSession(int numberSession) throws RemoteException;
	public boolean allSessionsFull() throws RemoteException;
}