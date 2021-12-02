import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class TimeServer {
	public static void main(String[] args) throws UnknownHostException {
		if(args.length != 2) {
			System.err.println("Numero di parametri inserito errato\n");
			System.err.println("Inserire l'indirizzo IP multicast a cui inviare il messaggio e la porta associata al gruppo multicast\n");
			System.exit(-1);
		}
		
		InetAddress addressDateGroup = InetAddress.getByName(args[0]);
		int portGroup = Integer.parseInt(args[1]);
		byte[] buffer = new byte[1024];
		
		try(DatagramSocket socket = new DatagramSocket()){
			if(addressDateGroup.isMulticastAddress() == false) {
				throw new IllegalArgumentException("Indirizzo IP inserito non è multicast " + addressDateGroup);
			}
			
			Calendar c = Calendar.getInstance();
			while(true) {
				String timeAndDate = c.getTime().toString();
				buffer = timeAndDate.getBytes();
				DatagramPacket message = new DatagramPacket(buffer, buffer.length, addressDateGroup, portGroup);
				socket.send(message);
				System.out.println("Messaggio inviato " + timeAndDate + "\n");
				Thread.sleep(ThreadLocalRandom.current().nextLong(100, 10000));
			}
		} catch (SocketException e) {
			System.err.println("Errore nel server " + e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Errore nel server " + e.getMessage());
			System.exit(-1);
		} catch (InterruptedException e) {
			System.err.println("Errore nel server" + e.getMessage());
			System.exit(-1);
		}
		
	}

}
