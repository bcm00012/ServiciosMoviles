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

import com.example.p1.Activity_Explorer.AdaptadorTitulares_Menudraw;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

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

public class Asynctask_DeleAndList extends AsyncTask<String, ComponentesLista, Integer> {

	int flag = 0;
	private Socket socket;

	DatosUsuario datosUser;
	BufferedReader in;
	String cancelar;
	PrintWriter out = null;
	Context context;
	Toast_app toast_app ;
	AdaptadorTitulares_pc adaptador_pc;
	ArrayList<ComponentesLista> datos_pc;
	String currentLine = "";
	String destinationFolder;
	String user;
	Toast_app toas_app;

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

	public Asynctask_DeleAndList(AdaptadorTitulares_Menudraw adaptador_menudraw,
			ArrayList<ComponentesListaDrawMenu> datos_menudraw, String user, Context context,Context contentToast,
			AdaptadorTitulares_pc adaptador_pc, ListView lstOpciones_pc, ArrayList<ComponentesLista> datos_pc,
			String destinationFolder) {
		this.adaptador_pc = adaptador_pc;
		this.context = context;
		this.socket = Sokect_Tcp.getInstance().getSocket();
		this.datos_pc = datos_pc;
		this.destinationFolder = destinationFolder;
		this.user = user;
		this.adaptador_menudraw = adaptador_menudraw;
		this.datos_menudraw = datos_menudraw;
		this.toast_app = new Toast_app(context);
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se
	 * ejecuta el método "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */
	@Override
	protected void onPreExecute() {
		Log.v("Asynctask", "Asynctask_DeleAndList onPreExecute");
		datos_pc.clear();
		adaptador_pc.notifyDataSetChanged();
		//Toast.makeText(context,"Subiendo la foto. ¡Tras ser moderada empezara a ser votada!: ", Toast.LENGTH_LONG).show();

		//	toas_app.borradoOk();
//		
//			toas_app.borradoErr();
		
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
		Log.v("Asynctask", "Asynctask_DeleAndList doInBackground");

		try {
			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
			in = new BufferedReader( 
					new InputStreamReader(this.socket.getInputStream()));

			out.println("dele " + destinationFolder);
			if (in.readLine().contentEquals("200 OK")) {
				//toas_app.borradoOk();
				//Toast.makeText(context,"Archivo borrado Correctamente", Toast.LENGTH_LONG).show();

			} else {
				//toas_app.borradoErr();
			//	Toast.makeText(context,"Error al borrar el Archivo", Toast.LENGTH_LONG).show();
			}
			out.println("path");
			if(in.readLine().equalsIgnoreCase("C:\\ftpserver")== false){
				publishProgress(new ComponentesLista("Atras", R.drawable.ic_atras));
			}

			out.println("list");
			in.readLine();
			


			while ((currentLine = in.readLine()) != null) {
				Log.v("Asynctask", "Asynctask_DeleAndList doInBackground while, Contenido del buffer: "+ currentLine);

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
		Log.v("Asynctask", "Asynctask_DeleAndList onProgressUpdate");
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
		Log.v("Asynctask", "Asynctask_DeleAndList onPostExecute");

		super.onPostExecute(cantidadProcesados);
		UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		DateTime dt = new DateTime();
		db.execSQL("INSERT INTO " + user + "(nomb_op,estado_op,fecha_op,hora_op) VALUES('Borrado Servidor','OK'," + "'"
				+ dt.getCurrentDate() + "'," + "'" + dt.getCurrentHour() + "')");
		String id, nomb, estado, fecha, hora;
		Cursor c = db.rawQuery(" SELECT * FROM " + user, null);
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

		new Asynctask_QueryBD_ListHistory(user, context, adaptador_menudraw, datos_menudraw).execute();
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
		Log.v("Asynctask", "Asynctask_DeleAndList onCancelled");

		UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		DateTime dt = new DateTime();
		db.execSQL("INSERT INTO " + user + "(nomb_op,estado_op,fecha_op,hora_op) VALUES('Borrado Servidor','BAD'," + "'"
				+ dt.getCurrentDate() + "'," + "'" + dt.getCurrentHour() + "')");

		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();
	}

}
