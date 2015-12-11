package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

public class ServicioDescarga extends Service implements Runnable {
	DatosUsuario datosUser;
	String nombreArchivo;
	AdaptadorTitulares_movil adaptador_movil;
	ArrayList<ComponentesLista> datos_movil;
	Context c;
BufferedReader entrada;
	public ServicioDescarga() {
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
		Log.d("Servicio", "ServicioDescarga onStartCommand...");

		datosUser = (DatosUsuario) intent.getExtras().getSerializable("datosUser");
		nombreArchivo = intent.getStringExtra("nombreArchivo");
		adaptador_movil = (AdaptadorTitulares_movil) intent.getParcelableExtra("myData");
		datos_movil = intent.getParcelableArrayListExtra("myData2");

		new Thread(this).start();

		return START_REDELIVER_INTENT;
	}

	@Override
	public void run() {

		try {
			Log.d("Servicio", "ServicioDescarga Entra en el hilo para descargar del servicio");
			Socket ss = Sokect_Tcp.getInstance().getSocket();
			PrintWriter outCommand = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())),
					true);
			
			entrada = new BufferedReader( new InputStreamReader(ss.getInputStream()));
			
			outCommand.println("retr " + nombreArchivo);
			entrada.readLine();

			Socket socket = new Socket(datosUser.getIp(), datosUser.puerto - 1);
			InputStream in = socket.getInputStream();

			boolean mExternalStorageAvailable = false;
			boolean mExternalStorageWriteable = false;
			String state = Environment.getExternalStorageState();
			Log.v("Servicio", "ServicioDescarga ESTE ES EL SOCKET DEL CLIENTE");

			// COMPROBACION DEL ALMACENAMIENTO EXTERNO
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				Log.v("ficheros2", "Podremos leer y escribir en ella");
				mExternalStorageAvailable = mExternalStorageWriteable = true;
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				Log.v("Servicio", "ServicioDescarga En este caso solo podremos leer los datos");

				mExternalStorageAvailable = true;
				mExternalStorageWriteable = false;
			} else {
				Log.v("Servicio", "No podremos leer ni escribir en ella");

				mExternalStorageAvailable = mExternalStorageWriteable = false;
			}

			if (mExternalStorageWriteable == true) {

				File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						"FTPPRETY");
				path.mkdirs();

				OutputStream out = new FileOutputStream(path + "/" + nombreArchivo);

				copy(in, out);
				// outCommand.close();
				out.close();
				in.close();
				socket.close();

			}
			Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "final");
			this.sendBroadcast(i);

			this.stopSelf();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			stopSelf();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stopSelf();
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

		Log.d("Servicio", "ServicioDescarga destruido...");
	}

}
