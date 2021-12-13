package classes;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ThreadLocalRandom;
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
					try{
						server.printSession(ThreadLocalRandom.current().nextInt(1, 50));
					}catch(RemoteException e) {
						System.out.println(e.getMessage() + "\n");
					}
					
				}
			}
			
			System.out.println("Tutto occupato, termino esecuzione\n");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void addSpeaker(String[] names, ServerInterface server) throws RemoteException {
		String newName = names[ThreadLocalRandom.current().nextInt(names.length)];
		int numberSession = ThreadLocalRandom.current().nextInt(1, 50);
		
		try {
			server.subscribeToSession(numberSession, newName);
		}catch(RemoteException e) {
			System.out.println(e.getMessage() + "\n");
		}
		
		
	}

}