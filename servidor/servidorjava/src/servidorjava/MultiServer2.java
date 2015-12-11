package servidorjava;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer2 extends Thread{
	
	Socket socket ;
	String currentPatch ,fileDownload;
	ServerSocket ss;
	OutputStream out;
    MultiServer2(Socket socket,String currentPatch , String fileDownload,ServerSocket ss) {
        this.socket = socket;
        this.currentPatch = currentPatch;
        this.fileDownload = fileDownload;
        this.ss=ss;
    }

    public void run() {
    	
    	

       
		try {
			
			System.out.println("ruta donde surbir el archivo "+currentPatch);
		System.out.println("Nombre del archivo "+fileDownload);
			out = new FileOutputStream(currentPatch+"\\"+fileDownload);
        //InputStream in = new FileInputStream(currentPatch);
		
		
		InputStream in = socket.getInputStream();
        
		 copy(in, out);
         // outCommand.close();
          out.close();
          out.flush();
          in.close();
          socket.close();
       // ss.close();
        
        System.out.println("Termina la subida");
		 } catch (Exception e) {
			 e.printStackTrace();
		}
    	
    }
    
    
    
    static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }
   
}


