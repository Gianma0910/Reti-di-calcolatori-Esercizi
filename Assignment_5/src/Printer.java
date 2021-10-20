import java.io.*;

public class Printer {
	private OutputStream output;
	
	public Printer(File output) {
		try {
			this.output = new FileOutputStream(output);
		} catch (FileNotFoundException e) {
			this.output = null;
			System.err.println("Impossible creare lo stream per l'output");
		}
	}

	public synchronized void write_into_file(String msg) throws IOException {
		if(output == null)
			return;
		
		output.write(msg.getBytes());
		output.write(';');
		output.write(' ');
	}
	
	
	public void close() throws IOException {
		output.flush();
		output.close();
	}
}
