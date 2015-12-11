package com.example.p1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsuariosSQLiteHelper extends SQLiteOpenHelper {

	private static int version = 1;
	private static String name = "AutenticaDb";
	private static CursorFactory factory = null;
//
	public UsuariosSQLiteHelper(Context context) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(this.getClass().toString(), "Creando base de datos");
		db.execSQL("CREATE TABLE AUTENTICA(" + " id INTEGER PRIMARY KEY," + " usuario TEXT, " + " password TEXT  )");

		Log.v("conexionn", "Tabla AUTENTICA creada");

		/*
		 * Insertamos datos iniciales
		 */

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}