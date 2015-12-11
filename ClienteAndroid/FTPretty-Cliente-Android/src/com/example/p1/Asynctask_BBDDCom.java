package com.example.p1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class Asynctask_BBDDCom extends AsyncTask<String, Float, Integer> {


	DatosUsuario datosUser;
	int mensajesrecibidos = 0;
	int flag = 0;
	Context context;


	public Asynctask_BBDDCom(Context context) {
		this.context = context;
	}

	/**
	 * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se
	 * ejecuta el método "doInBackground" en Segundo Plano
	 * 
	 * Se ejecuta en el hilo: PRINCIPAL
	 */
	@Override
	protected void onPreExecute() {

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
		Log.v("Asyntask doInBackgrounde", "Asyntask doInBackgrounde ");

		try {

			String usuario = null;
			String contraseña = null;

			String id = null;
			UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);

			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery(" SELECT * FROM AUTENTICA ", null);
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do {
					id = c.getString(0);
					usuario = c.getString(1);
					contraseña = c.getString(2);

				} while (c.moveToNext());
			}
			Log.v("Asynctask", "Asynctask_BBDDCom Datos iniciales  insertados");

			if (id != null) {	//Si existe un usuario pasa directacmente a Activity_Explorer
				flag = 1;
				datosUser = new DatosUsuario(usuario, contraseña, "dsg", (short) 21);
			}
		} catch (Exception e) {

		}

		return mensajesrecibidos;
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

		if (flag == 1) {

			Intent intent = new Intent(context, ActivityDos.class);
			intent.putExtra("datosUser", datosUser); // Paso deparametros al
														// nuevo activity
			intent.putExtra("userBd", "");
			intent.putExtra("passBd", "");	
			context.startActivity(intent);
			((Activity) context).finish();

		}
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

	}

}
