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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.p1.Fragment_Lista_Movil.ViewHolder;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Asynctask_List extends AsyncTask<String, ComponentesLista, Integer> {

	int flag = 0;
	private Socket socket;
	DatosUsuario datosUser;
	BufferedReader in;
	String cancelar;
	PrintWriter out = null;
	Context context;
	Toast_app toast_app;
	AdaptadorTitulares_pc adaptador_pc;

	ArrayList<ComponentesLista> datos_pc;
	String currentLine = "";

	/**
	 * Contructor de ejemplo que podemos crear en el AsyncTask
	 * 
	 * @param en
	 *            este ejemplo le pasamos un booleano que indica si hay más de
	 *            100 archivos o no. Si le pasas true se cancela por la mitad
	 *            del progreso, si le pasas false seguirá hasta el final sin
	 *            cancelar la descarga simulada
	 */

	public Asynctask_List(Context context, AdaptadorTitulares_pc adaptador_pc, ArrayList<ComponentesLista> datos_pc) {
		this.adaptador_pc = adaptador_pc;
		this.context = context;
		this.datos_pc = datos_pc;
		this.socket = Sokect_Tcp.getInstance().getSocket();
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

		datos_pc.clear();
		adaptador_pc.notifyDataSetChanged();
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
		Log.v("Asynctask", "Asynctask_List doInBackground");

		try {

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
			in = new BufferedReader( 
					new InputStreamReader(this.socket.getInputStream()));

			

			
			out.println("path");
			if(in.readLine().equalsIgnoreCase("C:\\ftpserver")== false){
				publishProgress(new ComponentesLista("Atras", R.drawable.ic_atras));
			}

			out.println("list");
			in.readLine();
			
			while ((currentLine = in.readLine()) != null) {
				Log.v("Asynctask", "Asynctask_List doInBackground while, Contenido buffer de entrada: " + currentLine);
				if (currentLine.equalsIgnoreCase(".")) {
					currentLine = null;
					break;
				}

				if (currentLine.contains(".txt")) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_texto));
				} else if (currentLine.contains(".docx") || (currentLine.contains(".doc"))) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_word));
				} else if (currentLine.contains(".jpg") || (currentLine.contains(".png"))
						|| (currentLine.contains(".bmp")) || ((currentLine.contains(".gif")))) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_foto));
				} else if (currentLine.contains(".pdf")) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_pdf));
				} else if (currentLine.contains(".mp3") || (currentLine.contains(".wma"))
						|| (currentLine.contains(".wav"))) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_music));
				} else if (currentLine.contains(".mp4") || (currentLine.contains(".avi"))) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_video));
				} else if (currentLine.contains(".")) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_unknow));
				} else if (currentLine.contains(".") == false) {
					publishProgress(new ComponentesLista(currentLine, R.drawable.list_carpeta));
				}

			}

		} catch (UnknownHostException e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			flag = 2;
			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
			Log.v("Conexion", "SISI CANCELADO");
		} catch (IOException e) {
			Log.v("Asyntask doInBackgrounde", "Fallo conexion ");
			flag = 2;
			e.printStackTrace();
			cancelar = "No es posible establecer la conexion con el servidor";
			cancel(true);
		} catch (Exception e) {
			flag = 2;
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
	protected void onProgressUpdate(ComponentesLista... currentLine) {
		Log.v("Asynctask", "Asynctask_List onProgressUpdate");
		datos_pc.add(currentLine[0]);
		adaptador_pc.notifyDataSetChanged();
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
		Log.v("Asynctask", "Asynctask_List onPostExecute");

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

		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();
	}

}
