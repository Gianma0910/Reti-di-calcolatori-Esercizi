import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ThreadLocalRandom;

public class ServerPING {

	static int port;
	static int seed;
	
	public static void main(String[] args) throws InterruptedException {
		if(args.length != 2) {
			System.err.println("Numero di argomenti inseriti errato\n");
			System.err.println("Inserire la porta del server e il seed\n");
			return;
		}
		
		port = Integer.parseInt(args[0]);
		seed = Integer.parseInt(args[1]);
		
		if(seed <= 0) {
			System.err.println("ERR -arg 2\n");
			return;
		}
		
		byte [] request = new byte[30];
		
		try{
			DatagramSocket socket = new DatagramSocket(port);
			System.out.println("Server is running...\n");
			int i = 0;
			while(i < 10) {
				int latency = ThreadLocalRandom.current().nextInt(100, 3000);
				int percentagePacketLoss = ThreadLocalRandom.current().nextInt(1, 101); 
				DatagramPacket requestPING = new DatagramPacket(request, request.length);
				socket.receive(requestPING);

				if(percentagePacketLoss >= 25) {
					DatagramPacket responsePING = new DatagramPacket(request, request.length, requestPING.getAddress(), requestPING.getPort());
					Thread.sleep(latency);
					socket.send(responsePING);
					String msg = new String(request, 0, responsePING.getLength());
					System.out.println("Indirizzo ip e porta del client : " + requestPING.getAddress() + "; " + requestPING.getPort() + "\n"
										+ msg + "\n" + "PING ritardato di : " + latency + "(ms)\n");
					i++;
				}else {
					String msg = new String(request, 0, requestPING.getLength());
					System.out.println("Indirizzo ip e porta del client : " + requestPING.getAddress() + "; " + requestPING.getPort() + "\n"
							+ msg + "\n" + "PING non inviato\n");
					i++;
				}
			}
			
			socket.close();
		} catch (SocketException e) {
			System.out.println("ERR -arg 1\n");
			return;
		} catch (IOException e) {
			System.err.println("ERR -arg 1\n");
			return;
		}
	}
}
