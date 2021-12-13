package classes;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import interfaces.ServerInterface;

public class ServerMain {

	public static void main(String[] args) {
		try {
			Server server = new Server();
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 5001);
			String name = "Server";
			LocateRegistry.createRegistry(5000);
			Registry reg = LocateRegistry.getRegistry(5000);
			reg.bind(name, stub);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}