import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {
	
	private static int port = 6789;

	public static void main(String[] args) throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		String messageServer;
		
		Selector selector = Selector.open();
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("Server is running...\n");
		ByteBuffer response = ByteBuffer.allocate(1024);
		
		while(selector.select() != 0){
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				
				if(key.isAcceptable()) {
					SocketChannel client = serverSocket.accept();
					client.configureBlocking(false);
					client.register(selector, SelectionKey.OP_READ);
					
					System.out.println("Nuovo client " + client.getRemoteAddress().toString() + "\n");
				}else if(key.isReadable()) {
					SocketChannel client = (SocketChannel) key.channel();
					
					//receive from client
					response.clear();
					int nBytes = client.read(response);
					
					messageServer = new String(response.array(), 0, nBytes);
					
					if(messageServer.equalsIgnoreCase("quit")) {
						System.out.println(client.getRemoteAddress().toString() + " disconnesso\n");
						client.close();
					}
					
					System.out.println(messageServer);
					
					//send to client
					response.clear();
					messageServer = messageServer.concat(" echoed by server");
					response.put(messageServer.getBytes());
					response.flip();
					client.write(response);
				}
			}
			
		}
		
		selector.close();
		serverSocket.close();
		
	}
}
