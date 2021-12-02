import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class TimeClient {
	
	public static void main(String[] args) throws UnknownHostException {
		if(args.length != 2) {
			System.err.println("Numero di parametri inserito errato\n");
			System.err.println("Inserire l'indirizzo IP multicast e la porta associata al gruppo multicast\n");
			System.exit(-1);
		}
		
		InetAddress dategroup = InetAddress.getByName(args[0]);
		int portGroup = Integer.parseInt(args[1]);
		byte[] buffer = new byte[1024];
		
		try(MulticastSocket socket = new MulticastSocket(portGroup)){
			if(dategroup.isMulticastAddress() == false) {
				throw new IllegalArgumentException("L'indirizzo IP inserito non è multicast: " + dategroup);
			}
			socket.joinGroup(dategroup);
			for(int i = 0; i < 10; i++) {
				DatagramPacket message = new DatagramPacket(buffer, buffer.length);
				socket.receive(message);
				String messageFromServer = new String(message.getData(), message.getOffset(), message.getLength());
				System.out.println("Ricevuto dal server: " + messageFromServer + "\n");
			}
		} catch (IOException e) {
			System.err.println("Errore nel client: " + e.getMessage());
		}
	}

}
