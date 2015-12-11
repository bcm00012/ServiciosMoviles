package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;
import com.example.p1.Activity_Explorer.AdaptadorTitulares_Menudraw;
import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Asynctask_DeleAndList_Movil extends AsyncTask<String, ComponentesLista, Integer> {


	BufferedReader in;
	String cancelar;

	Context context;
	AdaptadorTitulares_movil adaptador_pc;
	ArrayList<ComponentesLista> datos_pc;
	String destinationFolder;

	DatosUsuario datosUser;
	ArrayList<ComponentesListaDrawMenu> datos_menudraw;
	AdaptadorTitulares_Menudraw adaptador_menudraw;


	/**
	 * Contructor de ejemplo que podemos crear en el AsyncTask
	 * 
	 * @param en
	 *            este ejemplo le pasamos un booleano que indica si hay más de
	 *            100 archivos o no. Si le pasas true se cancela por la mitad
	 *            del progreso, si le pasas false seguirá hasta el final sin
	 *            cancelar la descarga simulada
	 */

	public Asynctask_DeleAndList_Movil(AdaptadorTitulares_Menudraw adaptador_menudraw,
			ArrayList<ComponentesListaDrawMenu> datos_menudraw, DatosUsuario datosUser, Context context,
			AdaptadorTitulares_movil adaptador_pc, ArrayList<ComponentesLista> datos_pc, String destinationFolder) {
		this.adaptador_pc = adaptador_pc;
		this.context = context;
		this.datos_pc = datos_pc;
		this.destinationFolder = destinationFolder;
		this.datosUser = datosUser;
		this.adaptador_menudraw = adaptador_menudraw;
		this.datos_menudraw = datos_menudraw;
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se
	 * ejecuta el método "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */
	@Override
	protected void onPreExecute() {
		Log.v("Asynctask", "Asynctask_DeleAndList_Movil onPreExecute");

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
		Log.v("Asynctask", "Asynctask_DeleAndList_Movil doInBackground");

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		// COMPROBACION DEL ALMACENAMIENTO EXTERNO
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Podremos leer y escribir en ella
			Log.v("Asynctask", "Podremos leer y escribir en ella");
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// En este caso solo podremos leer los datos
			Log.v("Asynctask", "En este caso solo podremos leer los datos");

			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// No podremos leer ni escribir en ella
			Log.v("Asynctask", "No podremos leer ni escribir en ella");

			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageWriteable == true) {

			File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"FTPPRETY");
			path.mkdirs();

			File borrar = new File(path, destinationFolder);

			borrar.delete();
			datos_pc.clear();

			String salida = "\r\n";
			String currentLine = "";

			File[] files = path.listFiles();
			for (int x = 0; x < files.length; x++) {
				Log.v("Asynctask", "Asynctask_DeleAndList_Movil Ficheros: " + files[x].getName());

				salida = salida + files[x].getName() + "\r\n";

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
		Log.v("Asynctask", "Asynctask_DeleAndList_Movil onProgressUpdate");

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
		Log.v("Asynctask", "Asynctask_DeleAndList_Movil onPostExecute");
		super.onPostExecute(cantidadProcesados);
		
		UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		DateTime dt = new DateTime();
		db.execSQL("INSERT INTO " + datosUser.getUser()
				+ "(nomb_op,estado_op,fecha_op,hora_op) VALUES('Borrado Cliente','OK'," + "'" + dt.getCurrentDate()
				+ "'," + "'" + dt.getCurrentHour() + "')");
		String id, nomb, estado, fecha, hora;
		Cursor c = db.rawQuery(" SELECT * FROM " + datosUser.getUser(), null);
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				id = c.getString(0);
				nomb = c.getString(1);
				estado = c.getString(2);
				fecha = c.getString(3);
				hora = c.getString(4);

				Log.v("Asynctask", id);
				Log.v("Asynctask", nomb);
				Log.v("Asynctask", estado);
				Log.v("Asynctask", fecha);
				Log.v("Asynctask", hora);

			} while (c.moveToNext());
		}

		new Asynctask_QueryBD_ListHistory(datosUser.getUser(), context, adaptador_menudraw, datos_menudraw).execute();
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
		Log.v("Asynctask", "Asynctask_DeleAndList_Movil onCancelled");

		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();
	}

}
