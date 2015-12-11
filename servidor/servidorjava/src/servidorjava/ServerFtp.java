package servidorjava;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.*;
public class ServerFtp {
	public static final int TCP_SERVICE_PORT = 21;		//Asignacion de puerto por defecto.
	static ServerSocket server = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Servidor> Iniciando servidor FTP");
		try {
			server = new ServerSocket(TCP_SERVICE_PORT);	//Crea el servidor socket. 
			while (true) {		//mientras sea distinto de true.
				final Socket newsocket = server.accept();	// objeto final que no podemos cambiar una vez inicializado.
				System.out.println("Servidor> Conexión entrante desde "	
						+ newsocket.getInetAddress().toString() + ":"
						+ newsocket.getPort());	//Mensaje dentro del servidor.
				new Thread(new Connection(newsocket)).start();	//Crea el objeto de la clase Connection y pasamos a ella,
			
			}
		} catch (IOException e) {
			System.err.println("Server "+e.getMessage());
			e.printStackTrace();
		}
	}

}
