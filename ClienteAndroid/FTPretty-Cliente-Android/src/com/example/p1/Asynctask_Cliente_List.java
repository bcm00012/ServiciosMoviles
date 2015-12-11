package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.concurrent.TimeUnit;

import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Asynctask_Cliente_List extends AsyncTask<String, ComponentesLista, Integer> {

	Context context;
	AdaptadorTitulares_movil adaptador_movil;
	ArrayList<ComponentesLista> datos;

	public Asynctask_Cliente_List(Context context, AdaptadorTitulares_movil adaptador_movil,ArrayList<ComponentesLista> datos) {
		this.adaptador_movil = adaptador_movil;
		this.context = context;
		this.datos = datos;
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se
	 * ejecuta el método "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */

	@Override
	protected void onPreExecute() {
		Log.v("Asynctask", "Asynctask_Cliente_List onPreExecute() ");

		datos.clear();
		adaptador_movil.notifyDataSetChanged();
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
		Log.v("Asynctask", "Asynctask_Cliente_List doInBackground");

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		// COMPROBACION DEL ALMACENAMIENTO EXTERNO
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Podremos leer y escribir en ella
			Log.v("Asynctask", "Asynctask_Cliente_List Podremos leer y escribir en ella");
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// En este caso solo podremos leer los datos
			Log.v("Asynctask", "Asynctask_Cliente_List En este caso solo podremos leer los datos");

			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// No podremos leer ni escribir en ella
			Log.v("Asynctask", "Asynctask_Cliente_List No podremos leer ni escribir en ella");

			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageWriteable == true) {

			File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"FTPPRETY");
			path.mkdirs();
			String salida = "\r\n";
			String currentLine = "";
			File[] files = path.listFiles();
			
			for (int x = 0; x < files.length; x++) {
				salida = salida + files[x].getName() + "\r\n";

				Log.v("Asynctask", "Asynctask_Cliente_List Lectura de ficheros for(): " + files[x].getName());
				currentLine = files[x].getName();
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
		Log.v("Asynctask", "Asynctask_Cliente_List onProgressUpdate");

		datos.add(currentLine[0]);
		adaptador_movil.notifyDataSetChanged();
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
		Log.v("Asynctask", "Asynctask_Cliente_List onPostExecute");

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
		Log.v("Asynctask", "Asynctask_Cliente_List onCancelled");

	}

}