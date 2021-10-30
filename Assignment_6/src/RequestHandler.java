import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RequestHandler implements Runnable {
	private Socket client;
	
	public RequestHandler(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		System.out.println("Client connesso");

		BufferedReader inFromClient;
		String request_line;
		
		while(true) {
			try {
				inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
				if(!(request_line = inFromClient.readLine()).isEmpty()) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			sendResponse(request_line, client);
			inFromClient.close();
			client.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client disconnesso");
	}

	
	private void sendResponse(String request_line, Socket client) throws IOException {
		OutputStream out_to_client = client.getOutputStream();
		
		StringTokenizer tokenizer = new StringTokenizer(request_line, "/ ");
		StringBuilder response_header;
		String extension_file;
		
		final String base_path = Server_HTTP.path;
		
		ArrayList<String> request_line_to_array = new ArrayList<>();
		
		while(tokenizer.hasMoreTokens()) {
			request_line_to_array.add(tokenizer.nextToken());
		}
		
		request_line = base_path + request_line_to_array.get(1);
		
		File requested_file = new File(request_line);
		FileInputStream in;
		System.out.println(requested_file);
		try {
			in = new FileInputStream(requested_file);
		}catch(FileNotFoundException e) {
			System.out.println("File non trovato!");
			out_to_client.write(fileNotFoundMessageBuilder());
			return;
		}
		
		
		int index = request_line_to_array.get(1).indexOf(".");
		extension_file = request_line_to_array.get(1).substring(index);
		
		System.out.println("Sto provvedendo per la risorsa " + requested_file);
		response_header = new StringBuilder("HTTP/1.1 200 OK\r\n" + "Server: SampleJavaServer\r\n");
	
		switch(extension_file) {
			case ".txt" :
				response_header.append("Content-Type: text/plain\r\n");
				break;
			case ".jpg" : 
				response_header.append("Content-Type: image/jpeg\r\n");
				break;
			case ".html" :
				response_header.append("Content-Type: text/html\r\n");
				break;
			case ".gif" :
				response_header.append("Content-Type: image/gif\r\n");
				break;
			default :
				break;
		}	
		
		response_header.append("\r\n");
	
		try {
			byte [] buffer = new byte[(int) requested_file.length()];
		
			if(in.read(buffer) == -1) {
				System.out.println("Fallimento nella lettura del file");
				out_to_client.close();
				return;
			}
			
			out_to_client.write(response_header.toString().getBytes());
			out_to_client.write(buffer);
			out_to_client.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] fileNotFoundMessageBuilder() {
		String message = "HTTP/1.1 404 Not Found\r\n" + "Server: SampleJavaServer\r\n" + 
				"Content-Type: text/html\r\n\r\n" + "<h1>ERROR 404</h1>";
	
		return message.getBytes();
	} 
	
}
