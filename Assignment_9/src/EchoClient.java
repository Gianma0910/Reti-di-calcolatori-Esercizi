import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class EchoClient {

	private static int portServer = 6789;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		try {
			SocketAddress address = new InetSocketAddress(portServer);
			SocketChannel socket = SocketChannel.open(address);
			socket.configureBlocking(true);
			String messageClient;
			
			System.out.println("Inserire parole o frasi...\n");
			
			while(!(messageClient = scan.nextLine()).trim().equals("quit")) {
				if(messageClient.isEmpty()) continue;
				
				System.out.println("Invio di: " + messageClient);
				
				//send messageClient to server
				buffer.put(messageClient.getBytes());
				buffer.flip();
				while(buffer.hasRemaining()) socket.write(buffer);
				buffer.clear();
				
				//receive echo response from server
				int nBytes = socket.read(buffer);
				String response = new String(buffer.array(), 0, nBytes);
				buffer.flip();
				
				System.out.println("Ricevuto: " + response + "\n");
				
			}
			
			//if messageClient == "quit" send to server and then close che SocketChannel
			buffer.put(messageClient.getBytes());
			buffer.flip();
			while(buffer.hasRemaining()) socket.write(buffer);		
			buffer.clear();
			scan.close();
			socket.close();
			
		}catch(IOException e) {
			System.err.println("Server disconnesso\n");
			return;
		}
		
	}

}
