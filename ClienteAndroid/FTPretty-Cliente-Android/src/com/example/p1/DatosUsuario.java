package com.example.p1;

import java.io.Serializable;
import java.net.Socket;

public class DatosUsuario implements Serializable {

	// Clase en la que es almacenada los datos de usuario.

	private String user, pass, ip;
	short puerto;

	public DatosUsuario(String user, String pass, String ip, short puerto) { 

		// Se establece los valores del usuario en la instanciacion de la clase.

		this.user = user;
		this.pass = pass;
		this.ip = ip;
		this.puerto = puerto;
	}

	// Metodos getter and setter.

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public short getPuerto() {
		return puerto;
	}

	public void setPuerto(short puerto) {
		this.puerto = puerto;
	}

}
