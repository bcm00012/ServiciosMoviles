package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class ServicioSubida extends Service implements Runnable {
	
	String nombreArchivo;
	PrintWriter salida = null;
	BufferedReader entrada;
	FileInputStream in;
	Socket socket;
	String dns = "pifa.ddns.net";
	InetAddress ipaddress;
	DatosUsuario datosUser;

	public ServicioSubida() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("Servicio", "ServicioSubida onStartCommand...");

		datosUser = (DatosUsuario) intent.getExtras().getSerializable("datosUser");
		nombreArchivo = intent.getStringExtra("nombreArchivo");

		Toast.makeText(getApplicationContext(), "Subiendo: ", Toast.LENGTH_SHORT).show();

		new Thread(this).start();

		return START_REDELIVER_INTENT;
	}

	@Override
	public void run() {
		Log.d("Servicio", "ServicioSubida run()");

		try {
			Socket ss = Sokect_Tcp.getInstance().getSocket();
			File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					"FTPPRETY");
			path.mkdirs();

			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())), true);
			entrada = new BufferedReader( // Objeto de la clase BufferedReader
											// con el que recibimos los Stream
											// de la aplicacion cliente.
					new InputStreamReader(ss.getInputStream()));
		

			salida.println("stor " + nombreArchivo);
			entrada.readLine();
			
			Socket socket = new Socket(datosUser.getIp(), datosUser.getPuerto() - 1);
			in = new FileInputStream(path.getAbsolutePath() + "/" + nombreArchivo);

			OutputStream out = socket.getOutputStream();
			copy(in, out);
			out.flush();
			out.close();
			in.close();

			socket.close();

			Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "final2");
			this.sendBroadcast(i);

			this.stopSelf();

		} catch (IOException e) {
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

	@Override
	public void onDestroy() {
		Log.d("Servicio", "ServicioSubida destruido...");
	}

}
