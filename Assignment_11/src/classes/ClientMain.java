package classes;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ThreadLocalRandom;

import interfaces.ClientInterface;
import interfaces.ServerInterface;

public class ClientMain {
	
	private static String [] names = {"Gianmarco", "Gabriele", "Lorenzo", "Davide", "Francesco", "Tommaso", "Jacopo", "Gabriel", "Dora", "Massimo", "Gabriella", "Giuseppe"};
	
	public static void main(String[] args) {
		try {
			System.out.println("Cerco il server");
			Registry reg = LocateRegistry.getRegistry(5000);
			String name = "Server";
			ServerInterface server = (ServerInterface) reg.lookup(name);
			
			while(server.allSessionsFull() == false) {
				int choose = ThreadLocalRandom.current().nextInt(2);
				if(choose == 0) {
					addSpeaker(names, server);
				}else {
					server.printSession(ThreadLocalRandom.current().nextInt(1, 36));
				}
			}
			
			System.out.println("Tutto occupato, termino esecuzione\n");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void addSpeaker(String[] names, ServerInterface server) throws RemoteException {
		String newName = names[ThreadLocalRandom.current().nextInt(names.length)];
		ClientInterface c = new Client(newName);
		ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject(c, 0);
		int numberSession = ThreadLocalRandom.current().nextInt(1, 50);
		
		server.subscribeToSession(numberSession, stub);
	}

}