import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Timestamp;

public class ClientPING {
	
	static int port;
	static String nameServer;
	static final int receiveTimeout = 2000;
	
	public static void main(String[] args) throws UnknownHostException {
		if(args.length < 2) {
			System.err.println("Numero di argomenti errato\n");
			System.err.println("Inserire nome e porta del server\n");
		
			return;
		}else {
			port = Integer.parseInt(args[0]);
			nameServer = args[1];
			
			if(port != ServerPING.port) {
				System.err.println("ERR -arg 1");
				return;
			}else if(nameServer != ServerPING.name) {
				System.err.println("ERR -arg 2");
				return;
			}else if(port != ServerPING.port && nameServer != ServerPING.name) {
				System.err.println("ERR -arg 1");
				System.err.println("ERR -arg 2");
				return;
			}
		}
		
		InetAddress serverAddress = InetAddress.getByName(nameServer);
		int packetReceived = 0;
		long [] RTTPacket = new long[10]; 
		
		
		try(DatagramSocket socket = new DatagramSocket(port, serverAddress)){
			for(Integer i = 0; i < 10; i++) {
				String seqNumber = i.toString();
				Timestamp timePacketSend = new Timestamp(System.currentTimeMillis());
				String time = timePacketSend.toString();
				String message = seqNumber + " " + time;
				byte[] response = new byte[message.length()];		
				DatagramPacket requestPING = new DatagramPacket(message.getBytes(), message.length(), serverAddress, port);
				socket.send(requestPING);
				DatagramPacket responsePING = new DatagramPacket(response, response.length);
				
				try {
					socket.setSoTimeout(receiveTimeout);
					socket.receive(responsePING);
				}catch(SocketTimeoutException e) {
					System.out.println("*\n");
				}
				
				Timestamp timePacketReceive = new Timestamp(System.currentTimeMillis());
				System.out.println(responsePING.getData().toString() + "\n");
				packetReceived++;
				RTTPacket[i] = timePacketSend.getTime() - timePacketReceive.getTime();
				
			}
			
			int percentagePacketLost = (10 - packetReceived)*10;
			float minRTT = minRTT(RTTPacket);
			float avgRTT = avgRTT(RTTPacket);
			float maxRTT = maxRTT(RTTPacket);

//			---- PING Statistics ----
//
//			10 packets transmitted, 7 packets received, 30% packet loss, round-trip (ms) min/avg/max = 63/190.29/290
			
			System.out.println("---- PING Statistics ----");
			System.out.printf("10 packets transmitted, %d" + packetReceived + " packets received, %d"
					+ percentagePacketLost + "% packet loss, round-trim (ms) min/avg/max = %f"
					+ minRTT + "/%.2f" + avgRTT + "/%f" + maxRTT);

			
		}catch(SocketException e) {
			System.err.println("Errore nella creazione del socket\n");
			e.printStackTrace();
		}catch(UnknownHostException e) {
			System.err.println("Host non ricosciuto\n");
			e.printStackTrace();
		}catch(IOException e) {
			System.err.println("Errore di I/O, impossibile mandare o ricevere messaggi\n");
			e.printStackTrace();
		}
		
		
	}

	private static float maxRTT(long[] RTTPacket) {
		float max = RTTPacket[0];
		
		for(int i = 1; i < 10; i++) {
			if(RTTPacket[i] > max)
				max = RTTPacket[i];
		}
		
		return max;	
	}

	private static float avgRTT(long[] RTTPacket) {
		float avg = 0;
		float sum = 0;
		
		for(int i = 0; i < 10; i++) {
			sum += RTTPacket[i];
		}
		
		avg = sum / 10;
		return avg;
	}

	private static float minRTT(long[] RTTPacket) {
		float min = RTTPacket[0];
		
		for(int i = 1; i < 10; i++) {
			if(RTTPacket[i] < min)
				min = RTTPacket[i];
		}
		
		return min;
	}

}
