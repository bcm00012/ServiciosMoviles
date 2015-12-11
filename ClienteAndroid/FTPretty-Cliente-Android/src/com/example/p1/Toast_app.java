package com.example.p1;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Toast_app {
	Context context;
	TextView text;
	View layout;
	LayoutInflater inflater;

	public Toast_app(Context context) {
		this.context = context;
		Toast();
	}

	public void Toast() {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = inflater.inflate(R.layout.toast_layout, null);
		this.text = (TextView) layout.findViewById(R.id.text);
	}

	public void toastNoIp(String SERVER_IP, int SERVERPORT) {
		this.text.setText("Introduzca Ip y Puerto correcto.\nNo existe servidor FTP en la direccion:\nIP :" + SERVER_IP
				+ ".\nPuerto: " + SERVERPORT + ".");
		showToast();
	}

	public void toastNoAuthentication() {
		this.text.setText("Introduzca Usuario y contraseña correctos.");
		showToast();
	}
	
	public void directorioRaiz(){
		this.text.setText("Usted ya se encuentra en el directorio RAIZ\nC:\\ftpserver");
		showToast();
	}
	
	public void borradoOk(){
		this.text.setText("Archivo borrado Correctamente");
		showToast();
	}
	
	public void borradoErr(){
		this.text.setText("Error al borrar el Archivo");
		showToast();
	}
	
	public void showToast() {
		Toast toast = new Toast(this.context);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(this.layout);
		toast.show();
	}

}
