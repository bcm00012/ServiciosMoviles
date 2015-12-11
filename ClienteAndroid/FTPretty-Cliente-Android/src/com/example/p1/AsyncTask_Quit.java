package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTask_Quit extends AsyncTask<String, Float, Integer> {

	private Socket socket;
	BufferedReader in;
	String cancelar;
	PrintWriter out = null;
	Context context;
	Toast_app toast_app;
	Activity mActivity;


	/**
	 * Contructor de ejemplo que podemos crear en el AsyncTask
	 * 
	 * @param en
	 *            este ejemplo le pasamos un booleano que indica si hay más de
	 *            100 archivos o no. Si le pasas true se cancela por la mitad
	 *            del progreso, si le pasas false seguirá hasta el final sin
	 *            cancelar la descarga simulada
	 */

	public AsyncTask_Quit(Context context,Activity activity) {
		
		this.context = context;
		this.socket = Sokect_Tcp.getInstance().getSocket();
		this.mActivity=activity;
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se
	 * ejecuta el método "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */
	@Override
	protected void onPreExecute() {
		Log.v("Asynctask", "Asynctask_List onPreExecute");


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
	protected Integer doInBackground(String... variableNoUsada) {
		Log.v("Asynctask", "Asynctask_Quit doInBackground");

		try {

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
			in = new BufferedReader( 
					new InputStreamReader(this.socket.getInputStream()));

			

			
			out.println("quit");
			
			if(in.readLine().contentEquals("OK 200")){
				out.flush();
				out.close();
				Log.v("AsyncTask", "AsyncTask_Quit Socket Cerrado");
			}


			

		} catch (UnknownHostException e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
			Log.v("Conexion", "SISI CANCELADO");
		} catch (IOException e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
		} catch (Exception e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
			if (isCancelled())
				Log.v("Conexion", "SISI CANCELADO");
		}
		return 0;
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
	protected void onProgressUpdate(Float... currentLine) {
		Log.v("Asynctask", "Asynctask_QUIT onProgressUpdate");

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
	protected void onPostExecute(Integer cantidadProcesados) {
		Log.v("Asynctask", "Asynctask_QUIT onPostExecute");
		this.mActivity.finish();
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
	protected void onCancelled(Integer cantidadProcesados) {
		Log.v("Asynctask", "Asynctask_List onCancelled");
		this.mActivity.finish();
	}

}
