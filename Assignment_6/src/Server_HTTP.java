import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class Server_HTTP {
	private static int port = 6789;
	private static final int core_pool_size = 5;
	private static final int max_pool_size = 30;
	private static final int keep_alive_time = 20;
	static String path;
	
	public static void main(String[] args) throws IOException {
		if(args.length < 3) {
			System.out.println("Inserire numero di porta, file path (senza includere il file) e il numero di richieste prima dello shutdown\n");
			System.out.println("Inserire -u per illimitate richieste");
		
			return;
		}
		
		port = Integer.parseInt(args[0]);
		path = args[1] + "/";
		int requests_before_shutdown = -1;
		
		if(!args[2].equals("-u"))
			requests_before_shutdown = Integer.parseInt(args[2]);
		
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Socket alla porta " + port + " non trovato");
			return;
		}
		
		System.out.println("Server HTTP sulla porta " + port);
		
		LinkedBlockingQueue<Runnable> request_queue = new LinkedBlockingQueue<>(8);
		ThreadPoolExecutor clientExecutor = new ThreadPoolExecutor(core_pool_size, max_pool_size, keep_alive_time, TimeUnit.MILLISECONDS, request_queue);
		
		int request_count = 0;
		
		if(requests_before_shutdown != -1) {
			while(request_count < requests_before_shutdown) {
				acceptAndHandle(socket, clientExecutor);
				request_count++;
			}
		}else {
			while(true) {
				acceptAndHandle(socket, clientExecutor);
			}
		}
		
		clientExecutor.shutdown();
		socket.close();
	}
	
	
	private static void acceptAndHandle(ServerSocket socket, ThreadPoolExecutor clientExecutor) {
		Socket client;
		try {
			client = socket.accept();
			clientExecutor.submit(new RequestHandler(client));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
