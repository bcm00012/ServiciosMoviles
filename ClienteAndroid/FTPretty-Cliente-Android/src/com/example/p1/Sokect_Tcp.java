package com.example.p1;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class Sokect_Tcp implements Serializable {
	
	private static Sokect_Tcp mInstance = null;
	private Socket socket;

	public static Sokect_Tcp getInstance() {
		if (mInstance == null) {
			mInstance = new Sokect_Tcp();
		}
		return mInstance;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
