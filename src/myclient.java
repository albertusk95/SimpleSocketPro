import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class myclient {
	
	/**
	*	hostname / computer name / IP address milik server 
	*	ex: localhost
	*/
	private String svr_hostname;
	
	/**
	*	port yang digunakan server untuk menangkap 
	*	permintaan koneksi dari client. 
	*	Port ini haruslah port yang tidak dialokasikan 
	*	untuk proses lain.
	*/
	private int svr_port;
	
	/**
	*	objek input dan output stream 
	*	yang digunakan oleh socket untuk berkomunikasi
	*/
	private PrintWriter out;
	private BufferedReader in;
	
	// konstruktor
	public myclient(String svr_hostname, int svr_port) {
		this.svr_hostname = svr_hostname;
		this.svr_port = svr_port;
	}
	
	// prosedur koneksi terhadap server 
	public void run_client() throws IOException {
		
		try {
			
			/**
			*	membuat objek socket baru yang akan digunakan
			*	untuk melakukan permintaan koneksi terhadap server
			*	dan prosedur komunikasi dengan server.
			*	Objek socket ini akan berkomunikasi dengan objek socket 
			*	yang dibentuk oleh server setelah permintaan koneksi dari 
			*	client diterima.
			*/
			Socket clt_socket = new Socket(svr_hostname, svr_port);
			
			/** 
			*	mengaktifkan input dan output stream milik client  
			* 	out digunakan untuk mengirim data ke server 
			* 	in digunakan untuk menerima data dari server  
			*/
			out = new PrintWriter(clt_socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clt_socket.getInputStream()));
			
			/**
			* 	prosedur komunikasi dengan server 
			*/	
			communicateToServer();
			
		} catch(IOException e) {
			System.out.println("Exception in running client " + e.getMessage());
		}
		
	}
	
	// prosedur komunikasi dengan server 
	public void communicateToServer() throws IOException {
		
		String fromServer, fromUser;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	
		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				if (fromServer.equals("Bye."))
					break;

				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client: " + fromUser);
					out.println(fromUser);
				}
			}
		} catch (IOException e) {
			System.out.println("Error in communicateToServer " + e.getMessage());
		}
		
	}
	
	// main function
	public static void main(String[] args) throws IOException {
		try {
			myclient mc = new myclient(args[0], Integer.parseInt(args[1]));
			mc.run_client();
		} catch (IOException e) {
			System.out.println("Error in running client " + e.getMessage());
		}
	}
	
}