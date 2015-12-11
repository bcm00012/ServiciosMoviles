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

import com.example.p1.Activity_Explorer.AdaptadorTitulares_Menudraw;
import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Asynctask_QueryBD_ListHistory extends AsyncTask<String, ComponentesListaDrawMenu, Integer> {

	Context context;
	AdaptadorTitulares_Menudraw adaptador_menudraw;
	ArrayList<ComponentesListaDrawMenu> datos;
	String user;

	public Asynctask_QueryBD_ListHistory(String user, Context context, AdaptadorTitulares_Menudraw adaptador_menudraw,
			ArrayList<ComponentesListaDrawMenu> datos) {
		this.user = user;
		this.adaptador_menudraw = adaptador_menudraw;
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
		Log.v("Asynctask", "Asynctask_QueryBD_ListHistory onPreExecute");

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
		Log.v("Asynctask", "Asynctask_QueryBD_ListHistory doInBackground");

		UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		datos.clear();
		String id = "";
		String nombre = "";
		String estado = "";
		String fecha = "";
		String hora = "";
		Cursor c = db.rawQuery(" SELECT * FROM " + user, null);
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				id = c.getString(0);
				nombre = c.getString(1);
				estado = c.getString(2);
				fecha = c.getString(3);
				hora = c.getString(4);

				if (estado.equalsIgnoreCase("OK")) {
					publishProgress(new ComponentesListaDrawMenu(id, nombre, fecha, hora, R.drawable.ic_ok));
				}

				if (estado.equalsIgnoreCase("BAD")) {
					publishProgress(new ComponentesListaDrawMenu(id, nombre, fecha, hora, R.drawable.ic_bad));
				}

			} while (c.moveToNext());
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
	protected void onProgressUpdate(ComponentesListaDrawMenu... currentLine) {
		Log.v("Asynctask", "Asynctask_QueryBD_ListHistory onProgressUpdate");

		datos.add(currentLine[0]);
		adaptador_menudraw.notifyDataSetChanged();
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
		Log.v("Asynctask", "Asynctask_QueryBD_ListHistory onPostExecute");

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
		Log.v("Asynctask", "Asynctask_QueryBD_ListHistory onCancelled");

	}

}