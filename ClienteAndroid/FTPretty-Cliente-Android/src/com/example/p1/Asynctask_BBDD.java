package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

class Asynctask_BBDD extends AsyncTask<String, Float, Integer> {

	// int flag=0;
	// private Socket socket;
	// private static final int SERVERPORT = 21; //Puerto por el que
	// conectaremos la sesion de control.
	// private static final String SERVER_IP = "192.168.0.44"; //Ip del servidor
	// Java FTP.
	DatosUsuario datosUser;

	int mensajesrecibidos = 0;

	Context context;

	/**
	 * Contructor de ejemplo que podemos crear en el AsyncTask
	 * 
	 * @param en
	 *            este ejemplo le pasamos un booleano que indica si hay m�s de
	 *            100 archivos o no. Si le pasas true se cancela por la mitad
	 *            del progreso, si le pasas false seguir� hasta el final sin
	 *            cancelar la descarga simulada
	 */
	public Asynctask_BBDD(DatosUsuario datosUser, Context context) {
		this.datosUser = datosUser;
		this.context = context;
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Despu�s de este se
	 * ejecuta el m�todo "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */
	@Override
	protected void onPreExecute() {

	}

	/**
	 * Se ejecuta despu�s de "onPreExecute". Se puede llamar al hilo Principal
	 * con el m�todo "publishProgress" que ejecuta el m�todo "onProgressUpdate"
	 * en hilo Principal
	 * 
	 * Se ejecuta en el hilo: EN SEGUNDO PLANO
	 * 
	 * @param array
	 *            con los valores pasados en "execute"
	 * @return devuelve un valor al terminar de ejecutar este segundo plano. Se
	 *         lo env�a y ejecuta "onPostExecute" si ha termiado, o a
	 *         "onCancelled" si se ha cancelado con "cancel"
	 */

	@Override
	protected Integer doInBackground(String... variableNoUsada) {
		Log.v("Asyntask doInBackgrounde", "Asyntask doInBackgrounde ");

		String user = datosUser.getUser(); 
		String pass = datosUser.getPass();
		String ip = datosUser.getIp();
		short puerto = datosUser.getPuerto();

		try {

			UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);

			SQLiteDatabase db = dbHelper.getWritableDatabase();

			db.execSQL("INSERT INTO AUTENTICA(id,usuario, password) VALUES(1,'" + user + "','" + pass + "')");

			Cursor c = db.rawQuery(" SELECT * FROM AUTENTICA ", null);
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya m�s registros
				do {
					String id = c.getString(0);
					String usuario = c.getString(1);
					String contrase�a = c.getString(2);

				} while (c.moveToNext());
			}


		} catch (Exception e) {

		}

		return mensajesrecibidos;
	}

	/**
	 * Se ejecuta despu�s de que en "doInBackground" ejecute el m�todo
	 * "publishProgress".
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 * 
	 * @param array
	 *            con los valores pasados en "publishProgress"
	 */
	@Override
	protected void onProgressUpdate(Float... porcentajeProgreso) {

	}

	/**
	 * Se ejecuta despu�s de terminar "doInBackground".
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 * 
	 * @param array
	 *            con los valores pasados por el return de "doInBackground".
	 */
	@Override
	protected void onPostExecute(Integer cantidadProcesados) {

	}

	/**
	 * Se ejecuta si se ha llamado al m�todo "cancel" y despu�s de terminar
	 * "doInBackground". Por lo que se ejecuta en vez de "onPostExecute" Nota:
	 * Este onCancelled solo funciona a partir de Android 3.0 (Api Level 11 en
	 * adelante). En versiones anteriores onCancelled no funciona
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 * 
	 * @param array
	 *            con los valores pasados por el return de "doInBackground".
	 */
	@Override
	protected void onCancelled(Integer cantidadProcesados) {

	}

}
