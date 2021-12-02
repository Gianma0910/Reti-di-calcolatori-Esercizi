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
					
					String request = receiveFromClient(client, response);
					if(request == null) return; 
					if(request.equals("quit")) {
						System.out.println(client.getRemoteAddress().toString() + " : disconnesso\n");
						client.close();
						return;
					}
					System.out.println(request);
					request = request.concat(" echoed by server");
					client.register(selector, SelectionKey.OP_WRITE, request);
				}else if(key.isWritable()) {
					SocketChannel client = (SocketChannel) key.channel();
					String request = (String) key.attachment();
				
					response.clear();
					response.put(request.getBytes());
					response.flip();
					while(response.hasRemaining()) client.write(response);
					
					client.register(selector, SelectionKey.OP_READ);
				}
			}
			
		}
		
		selector.close();
		serverSocket.close();
		
	}

	private static String receiveFromClient(SocketChannel client, ByteBuffer response) throws IOException {
		response.clear();
		int nBytes;
		
		StringBuilder request = new StringBuilder();
		while((nBytes = client.read(response)) > 0) {
			request.append(new String(response.array(), 0, nBytes));
		}
		
		if(nBytes == -1) {
			System.out.println(client.getRemoteAddress().toString() + " : disconnesso\n");
			client.close();
			return null;
		}
		
		return request.toString();
	}
}
