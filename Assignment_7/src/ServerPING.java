import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ServerPING {

	static final String name = "localhost";
	static int port;
	static long seed;
	
	public static void main(String[] args) {
		if(args.length < 2) {
			System.err.println("Numero di argomenti errato\n");
			System.err.println("Inserire il numero di porta e un seed per la generazione casuale della latenza e della perdita dei pacchetti\n");
			return;
		}else {
			port = Integer.parseInt(args[0]);
			seed = Integer.parseInt(args[1]);
			
			if(port < 0 || (port >= 0 && port <= 1024)) {
				System.err.println("ERR -arg 1");
				return;
			}else if(seed <= 0) {
				System.err.println("ERR -arg 2");
				return;
			}else if(port < 0 || (port >= 0 && port <= 1024) && seed <= 0) {
				System.err.println("ERR -arg 1");
				System.err.println("ERR -arg 2");
				return;
			}
		}
		
		byte [] requestAndResponse = new byte[30];
		
		try(DatagramSocket socket = new DatagramSocket(port)) {
			for(Integer i = 0; i < 10; i++) {
				long latency = ThreadLocalRandom.current().nextLong(seed);
				long percentagePacketLoss = ThreadLocalRandom.current().nextLong(0, seed);
				DatagramPacket requestPING = new DatagramPacket(requestAndResponse, requestAndResponse.length);
				socket.receive(requestPING);
				
				if(percentagePacketLoss <= ((seed*100)/25)) {
					System.out.println("Indirizzo IP e porta del Client: " + requestPING.getAddress() + "; " + requestPING.getPort()
										+ "\n" + "Messaggio di PING: " + requestPING.getData().toString() + "\n"
										+ "Azione server: PING non inviato");
				}else {
					DatagramPacket responsePING = new DatagramPacket(requestAndResponse, requestAndResponse.length, requestPING.getAddress(), requestPING.getPort());
					try {
						Thread.sleep(latency);
					}catch(InterruptedException e) {
						System.err.println("Thread interrotto prima della fine dello sleep\n");
						e.printStackTrace();
					}
					socket.send(responsePING);	
					System.out.println("Indirizzo IP e porta del Client: " + requestPING.getAddress() + "; " + requestPING.getPort()
										+ "\n" + "Messaggio di PING: " + requestPING.getData().toString() + "\n"
										+ "Azione server: PING ritardato di " + latency + " ms\n");
				}
			}
		}catch(SocketException e) {
			System.err.println("Errore nella creazione del socket\n");
			return;
		}catch(UnknownHostException e) {
			System.err.println("Host non riconosciuto\n");
			return;
		}catch(IOException e) {
			System.err.println("Errore di I/O, impossibile mandare e ricevere messaggi\n");
			return;
		}
	}

}
