package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

class Asynctask_Conexion extends AsyncTask<String, Float, String> {
	int flag = 0;
	Socket socket;

	String dns = "pifa.ddns.net";

	InetAddress ipaddress;
	float progreso = 0.0f;

	DatosUsuario datosUser;
	String textFromClient;
	BufferedReader in;
	TextView mensajeProgress;
	String cancelar;
	int mensajesrecibidos = 0;
	private ProgressBar barraProgress;
	PrintWriter out = null;
	Context context;
	Toast_app toast_app;
	String result;
	String ipServidor;
	short puerto;
	String userBd;
	String passBd;
	/**
	 * Contructor de ejemplo que podemos crear en el AsyncTask
	 * @param passBd 
	 * @param userBd 
	 * 
	 * @param en
	 *            este ejemplo le pasamos un booleano que indica si hay más de
	 *            100 archivos o no. Si le pasas true se cancela por la mitad
	 *            del progreso, si le pasas false seguirá hasta el final sin
	 *            cancelar la descarga simulada
	 */

	public Asynctask_Conexion(Context context, DatosUsuario datosUser, TextView mensajeProgress,
			ProgressBar barraProgress, String userBd, String passBd) {
		this.datosUser = datosUser;
		this.mensajeProgress = mensajeProgress;
		this.barraProgress = barraProgress;
		this.context = context;
		this.toast_app = new Toast_app(context);
		//datosUser.setIp("192.168.1.12");
		this.ipServidor = datosUser.getIp();
		//this.datosUser.setPuerto((short) 21);
		this.puerto = datosUser.getPuerto();
		this.userBd = userBd;
		this.passBd = passBd;
		// try {
		// this.ipaddress = InetAddress.getByName("pifa.ddns.net");
		// this.ip = this.ipaddress.getHostAddress();
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se
	 * ejecuta el método "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */

	@Override
	protected void onPreExecute() {
		Log.v("Asynctask", "Asynctask_Conexion onPreExecute() ");

		mensajeProgress.setText("Conectando con el Servidor FTP");
	}

	/**
	 * Se ejecuta después de "onPreExecute". Se puede llamar al hilo Principal
	 * con el método "publishProgress" que ejecuta el método "onProgressUpdate"
	 * en hilo Principal
	 * 
	 * Se ejecuta en el hilo: EN SEGUNDO PLANO
	 * 
	 * @param array
	 *            con los valores pasados en "execute"
	 * @return devuelve un valor al terminar de ejecutar este segundo plano. Se
	 *         lo envía y ejecuta "onPostExecute" si ha termiado, o a
	 *         "onCancelled" si se ha cancelado con "cancel"
	 */
	@Override
	protected String doInBackground(String... variableNoUsada) {
		Log.v("Asynctask", "Asynctask_Conexion doInBackgrounde");
		String user = "user " + datosUser.getUser(); // Recoge el nombre de
														// usuario para elviarlo
														// por el socket
		String pass = "pass " + datosUser.getPass();

		try {

			InetAddress serverAddr = InetAddress.getByName(ipServidor);
			this.socket = new Socket(serverAddr, puerto);

			Log.v("Conexion", "Entra el siguiente try");

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
			in = new BufferedReader( // Objeto de la clase BufferedReader con el
										// que recibimos los Stream de la
										// aplicacion cliente.
					new InputStreamReader(this.socket.getInputStream()));
			

			textFromClient = in.readLine();
			Log.v("Conexion", textFromClient);

			textFromClient = in.readLine();
			Log.v("Conexion", textFromClient);

			if (userBd.equalsIgnoreCase("")==false) {
				out.println("useb " + userBd);


				in.readLine();
				out.println("pasb " + passBd);


				in.readLine();
			}
			
			progreso += 33.33;
			publishProgress(progreso);
			mensajesrecibidos = mensajesrecibidos + 1;
			Log.v("Conexion establecida", "Conexion establecida con el servidor");

			while (mensajesrecibidos != 3) {

				if (progreso < 35) {
					out.println(user);
				} else {
					Log.v("while", "envia pass");

					out.println(pass);
				}
				Log.v("while", "amtes de leer");

				textFromClient = in.readLine();
				progreso += 33.33;
				Log.v("while", Integer.toString(mensajesrecibidos));

				mensajesrecibidos = mensajesrecibidos + 1;

				publishProgress(progreso);

				Log.v("while", textFromClient);

				if (textFromClient.contains("500")) {
					flag = 1;
					cancelar = "Usuario no existe";
					result = "Authentication Incorrecta";
					cancel(true);
				} else {
					// AUTENTICACION CORRECTAAA

					UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);

					SQLiteDatabase db = dbHelper.getWritableDatabase();

					db.execSQL("CREATE TABLE if not exists " + datosUser.getUser() + "("
							+ " id_op INTEGER PRIMARY KEY AUTOINCREMENT," + " nomb_op TEXT, " + " estado_op TEXT, "
							+ " fecha_op TEXT, " + " hora_op TEXT )");
					Log.v("Base de datos", " Base de datos " + user + " creada de ahora o ya creada");
				}
			}
			// try {
			// TimeUnit.SECONDS.sleep(3);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		} catch (UnknownHostException e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			flag = 2;
			result = "No es posible establecer la conexion con el servidor";
			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
			Log.v("Conexion", "SISI CANCELADO");
		} catch (IOException e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			flag = 2;
			result = "No es posible establecer la conexion con el servidor";

			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);

		} catch (Exception e) {
			flag = 2;
			result = "No es posible establecer la conexion con el servidor";

			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");

			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
			if (isCancelled())
				Log.v("Conexion", "SISI CANCELADO");
		}

		return "";
	}

	/**
	 * Se ejecuta después de que en "doInBackground" ejecute el método
	 * "publishProgress".
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 * 
	 * @param array
	 *            con los valores pasados en "publishProgress"
	 */
	@Override
	protected void onProgressUpdate(Float... porcentajeProgreso) {
		Log.v("Asynctask", "Asynctask_Conexion onProgressUpdate");

		if (progreso < 35) {
			mensajeProgress.setText("Conexion establecida con el Servidor FTP");
		} else if (progreso > 35 && progreso < 65) {
			mensajeProgress.setText("Authentication con el Servidor");

		}
		Log.v("Asyntask onProgressUpdate", "Progreso descarga: " + porcentajeProgreso[0] + "%. Hilo PRINCIPAL");

		barraProgress.setProgress(Math.round(porcentajeProgreso[0]));
	}

	/**
	 * Se ejecuta después de terminar "doInBackground".
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 * 
	 * @param array
	 *            con los valores pasados por el return de "doInBackground".
	 */
	@Override
	protected void onPostExecute(String cantidadProcesados) {
		Log.v("Asynctask", "Asynctask_Conexion onPostExecute");

		mensajeProgress.setText("Authentication Completada con exito");
		//mensajeProgress.setTextColor(Color.GREEN);
		Sokect_Tcp.getInstance().setSocket(socket);
		
		Intent intent = new Intent(context, Activity_Explorer.class);
		intent.putExtra("datosUser", datosUser); // Paso deparametros al nuevo
													// activity
		context.startActivity(intent);
		((Activity) context).finish();
	}

	/**
	 * Se ejecuta si se ha llamado al método "cancel" y después de terminar
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
	protected void onCancelled(String cantidadProcesados) {
		Log.v("Asynctask", "Asynctask_Conexion onCancelled");

		mensajeProgress.setText("Motivo del error: " + cancelar + ". Hilo PRINCIPAL");
		mensajeProgress.setTextColor(Color.RED);

		if (flag == 1) {
			toast_app.toastNoAuthentication();

		} else if (flag == 2) {
			toast_app.toastNoIp(ipServidor, puerto);

			UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.execSQL("DELETE FROM AUTENTICA WHERE id=1");

			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
			((Activity) context).finish();
		}

		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();
	}

}
