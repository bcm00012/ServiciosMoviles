package com.example.p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ActivityDos extends Activity {
	
	   DatosUsuario datosUser;
       String textFromClient,userBd="",passBd="";
       TextView mensajeProgress;
	

////////////////////////////////////////////////////////	onCreate Activity	////////////////////////////////////////////////////////
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("Activity", "ActivityDos");
		setContentView(R.layout.layout_progressbar);
		
		mensajeProgress = (TextView) findViewById(R.id.TextView_mensajesAlUsuario);
		ProgressBar barraProgress =(ProgressBar) findViewById(R.id.progressBar_indicador);
		barraProgress.setMax(100);
		datosUser = (DatosUsuario)getIntent().getExtras().getSerializable("datosUser");
		barraProgress.setDrawingCacheBackgroundColor(Color.BLUE);;
		userBd = getIntent().getStringExtra("userBd");
		passBd = getIntent().getStringExtra("passBd");
		
		new Asynctask_Conexion(this,datosUser,mensajeProgress,barraProgress,userBd,passBd).execute();
	}
	
}




