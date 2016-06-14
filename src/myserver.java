import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class myserver {
	
	/**
	*	port yang digunakan server untuk menangkap 
	*	permintaan koneksi dari client. 
	*	Port ini haruslah port yang tidak dialokasikan 
	*	untuk proses lain.
	*/
	private int port;
	
	/**
	*	objek input dan output stream 
	*	yang digunakan oleh socket untuk berkomunikasi
	*/
	private PrintWriter out;
	private BufferedReader in;
	
	// konstruktor
	public myserver(int port) {
		this.port = port;
	}
	
	// prosedur koneksi terhadap client 
	public void run_server() throws IOException {
		
		try {
			/**
			*	membuat server socket baru
			*/
			ServerSocket svr_socket = new ServerSocket(port);
			
			/** 
			*	menerima permintaan koneksi baru dari client
			* 	jika koneksi diterima, akan dibentuk objek socket baru 
			* 	yang diletakkan pada local port (listening port milik server)
			* 	dan memiliki informasi berupa address dan port milik client 
			*/
			Socket clt_socket = svr_socket.accept();
			
			/** 
			*	mengaktifkan input dan output stream milik server 
			* 	out digunakan untuk mengirim data ke client 
			* 	in digunakan untuk menerima data dari client 
			*/
			out = new PrintWriter(clt_socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clt_socket.getInputStream()));
		
			/**
			* 	prosedur komunikasi dengan client 
			*/
			communicateToClient();
			
		} catch (IOException e) {
			System.out.println("Exception in running server " + e.getMessage());
		}
		
	}
	
	// prosedur komunikasi dengan client 
	public void communicateToClient() throws IOException {
		
		String inputLine, outputLine;
        
		try {
			myprotocol mp = new myprotocol();
			outputLine = mp.processInput(null);
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = mp.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye."))
					break;
			}
		} catch (IOException e) {
			System.out.println("Error in communicateToClient " + e.getMessage());
		}
		
	}
	
	// main function
	public static void main(String[] args) throws IOException {
		try {
			myserver ms = new myserver(Integer.parseInt(args[0]));
			ms.run_server();
		} catch (IOException e) {
			System.out.println("Error in running server " + e.getMessage());
		}
	}
	
}