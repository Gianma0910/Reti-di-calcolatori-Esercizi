import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ClientPING {

	private static int portServer;
	private static String nameServer;
	private static int receiveTimeout = 2000;

	public static void main(String[] args) throws UnknownHostException {
		if(args.length != 2) {
			System.err.println("Numero di argomenti inseriti errato\n");
			System.err.println("Inserire il nome e la porta del server\n");
			return;
		}
	
		nameServer = args[0];
		portServer = Integer.parseInt(args[1]);
		InetAddress address;
		DatagramSocket socket; 
		ArrayList<Boolean> packetRefused = new ArrayList<>(Arrays.asList(new Boolean[10]));
		int packetReceived = 0;
		int seqNumber = 0;
		byte [] response;
		long [] RTTPacket = new long[10];

		Collections.fill(packetRefused, Boolean.FALSE);
		
		try {
			address = InetAddress.getByName(nameServer);
			socket = new DatagramSocket(0);
		} catch (SocketException e) {
			System.out.println("ERR -arg 2\n");
			return;
		}catch(UnknownHostException e) {
			System.err.println("ERR -arg 1\n");
			return;
		}
		
		try {
			while(seqNumber < 10){
				Timestamp timeSend = new Timestamp(System.currentTimeMillis());
				String messagePING = "PING " + seqNumber + " " + timeSend;
				DatagramPacket requestPING = new DatagramPacket(messagePING.getBytes(), messagePING.length(), address, portServer);
				socket.send(requestPING);
				int temp = seqNumber;
				seqNumber++;
				response = new byte[messagePING.length()];
				DatagramPacket responsePING = new DatagramPacket(response, response.length); 	
			
				try {
					socket.setSoTimeout(receiveTimeout);
					socket.receive(responsePING);
					Timestamp timeReceived = new Timestamp(System.currentTimeMillis());
					RTTPacket[temp] = timeReceived.getTime() - timeSend.getTime();
					String msg = new String(response, 0, responsePING.getLength());
					
					if(packetRefused.get(temp).equals(Boolean.FALSE)) {
						packetReceived++;
						System.out.println(msg + " RTT (ms) : " + RTTPacket[temp]);
						System.out.println("\n");
					}
				}catch(SocketTimeoutException e) {
					System.out.println("*\n");
					packetRefused.set(temp, Boolean.TRUE);
					RTTPacket[temp] = -1;
				}
			
			}
		}catch(IOException e) {
			System.err.println("ERR -arg 2\n");
			socket.close();
			return;
		}
		
		int percentagePacketLoss = (10 - packetReceived) * 10;
		long minRTT = minRTT(RTTPacket);
		long maxRTT = maxRTT(RTTPacket);
		float avgRTT = avgRTT(RTTPacket);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		System.out.println("---- PING Statistics ----");
		System.out.println("10 packets transmitted, " + packetReceived + " packets received, " + percentagePacketLoss + "% packet loss"
							+ "\n" + "round-trip (ms) min/avg/max " + minRTT + "/" + df.format(avgRTT) + "/" + maxRTT + "\n");
		
		socket.close();
	}

	private static float avgRTT(long[] RTTPacket) {
		float avg = 0;
		long sum = 0;
				
		for(int i = 0; i < 10; i++) {
			sum += RTTPacket[i];
		}		
		
		avg = sum / 10;
		
		return avg;
	}

	private static long maxRTT(long[] RTTPacket) {
		long max = RTTPacket[0];
		
		for(int i = 1; i < 10; i++) {
			if(max < RTTPacket[i])
				max = RTTPacket[i];
		}
		
		return max;
	}

	private static long minRTT(long[] RTTPacket) {
		long min = RTTPacket[0];
		
		for(int i = 1; i < 10; i++) {
			if(min > RTTPacket[i])
				min = RTTPacket[i];
		}
		
		return min;
	}
}
