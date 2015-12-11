package com.example.p1;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer extends Thread{
	
	Socket socket = null;
	String currentPatch ,fileDownload;
	ServerSocket ss;
    MultiServer(Socket socket,String currentPatch , String fileDownload,ServerSocket ss) {
        this.socket = socket;
        this.currentPatch = currentPatch;
        this.fileDownload = fileDownload;
        this.ss=ss;
    }

    public void run() {
    	
    	

        FileInputStream in;
		try {
			
		
        in = new FileInputStream(currentPatch+"\\"+fileDownload);
        //InputStream in = new FileInputStream(currentPatch);
       
        OutputStream out = socket.getOutputStream();
        copy(in, out);
        out.flush();
        out.close();
        in.close();
        
        socket.close();
        ss.close();
        
        System.out.println("Termina el envio");
		 } catch (Exception e) {

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


