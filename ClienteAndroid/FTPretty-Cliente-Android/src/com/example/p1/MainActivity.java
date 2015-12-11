package com.example.p1;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements Fragment_DatosUsuario.InterfazDatosUser, FragmentEmpezar.InterfazEmpezar,Fragment_RegistrarUsuario.InterfazRegistrar {
	
	public int currentimageindex = 0;
	ImageView slidingimage;
	Fragment fragment = new FragmentEmpezar(); // Objeto Fragment Boton empezar
	Fragment fragmento = new Fragment_DatosUsuario(); // Objeto Fragment Datos de ususario
	Fragment fragmentRegistrar = new Fragment_RegistrarUsuario(); // Objeto Fragment Boton empezar
String userBd="",passBd="";
	ImageButton mButton;
	EditText mEdit;
	private EditText lblMensaje;
	Context context = this;
	private int[] IMAGE_IDS = { // Array de imagenes del Carrusel
			R.drawable.banner, R.drawable.juande, R.drawable.bibi };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * Slide Fragment Estatico
		 */
		final Handler mHandler = new Handler();
		final Runnable mUpdateResults = new Runnable() {
			public void run() {
				AnimateandSlideShow();
			}
		};
		int delay = 1000; // delay for 1 sec.
		int period = 5000; // repeat every 4 sec.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				mHandler.post(mUpdateResults);
			}
		}, delay, period);

		/*
		 * Recupera Estado por la destruccion del activity
		 */
		if (savedInstanceState != null) { 

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			fragmento = getFragmentManager().getFragment(savedInstanceState, "fragmento"); // Recupera
																							// el
																							// Fragmenta
			transaction.replace(R.id.framelayout_contenedor_detalle, fragmento);
			transaction.commit(); // Inicializa el fragmento

		} else { // Si no se ha destruido entra en esta condicion y inicia el
					// Fragment Empezar

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.add(R.id.framelayout_contenedor_detalle, fragmento);
			transaction.replace(R.id.framelayout_contenedor_detalle, fragment).commit(); // Inicia
																							// el
																							// Fragment
																							// de
																							// entrada
																							// a
																							// la
																							// app

		}
		new Asynctask_BBDDCom(this).execute();
	}

	/*
	 * ///// Guarda el estado por la destruccion del activity /////
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) { // Metodo que Guarda
															// los datos ante la
															// destruccion de un
															// Activity
		super.onSaveInstanceState(outState);

		getFragmentManager().putFragment(outState, "fragmento", fragmento); // Guarda
																			// el
																			// estado
																			// del
																			// fragmento
																			// para
																			// recuperarlo
																			// con
																			// el
																			// nombre
																			// "fragmento"
																			// posteriormente
	}

	//////////////////////////////////////////////////////// Slide Fragment
	//////////////////////////////////////////////////////// Estatico
	//////////////////////////////////////////////////////// ////////////////////////////////////////////////////////

	public void onClick(View v) {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void AnimateandSlideShow() {
		slidingimage = (ImageView) findViewById(R.id.Carrusel);
		slidingimage.setImageResource(IMAGE_IDS[currentimageindex % IMAGE_IDS.length]);
		currentimageindex++;
		Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		slidingimage.setScaleType(ScaleType.FIT_XY);
		slidingimage.startAnimation(rotateimage);
	}

	//////////////////////////////////////////////////////// Interfaz evento
	//////////////////////////////////////////////////////// boton conectar
	//////////////////////////////////////////////////////// Fragment_DatosUsuario
	//////////////////////////////////////////////////////// ////////////////////////////////////////////////////////

	@Override
	public void conectarServidor(DatosUsuario datosUser) { // Metodo que hace
															// pasar al
															// ActivityDos.
															// Tambien se pasa
															// un objeto de la
															// clase
															// DatosUsuario al
															// ActivityDos
															// gracias al objeto
															// Intent
		// TODO Auto-generated method stub
		Intent i = new Intent(this, ActivityDos.class);
		i.putExtra("datosUser", datosUser); // Paso deparametros al nuevo
		i.putExtra("userBd", userBd);
		i.putExtra("passBd", passBd);											// activity
		startActivity(i); // Se inicia el ActivityDos
		finish();
	}

	//////////////////////////////////////////////////////// Interfaz evento
	//////////////////////////////////////////////////////// boton Empezar
	//////////////////////////////////////////////////////// FragmentEmpezar
	//////////////////////////////////////////////////////// ////////////////////////////////////////////////////////

	@Override
	public void onEntrada() { // Metodo en el que se entra cuando se pulsa el
								// boton del FRAGMENT Empezar y es mostrado el
								// Fragment de datos de usuario

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fragment_slide_left_enter, // Transicion
																			// para
																			// el
																			// paso
																			// entre
																			// fragmentos
				R.anim.fragment_slide_left_exit, R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
		transaction.hide(fragment).replace(R.id.framelayout_contenedor_detalle, fragmento);
		transaction.addToBackStack(null);
		transaction.commit(); // Inicio del Fragment Datos de usuario
	}
	
	@Override
	public void onRegistrar() { // Metodo en el que se entra cuando se pulsa el
								// boton del FRAGMENT Empezar y es mostrado el
								// Fragment de datos de usuario

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		transaction.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
		
		transaction.hide(fragment).replace(R.id.framelayout_contenedor_detalle, fragmentRegistrar);
		transaction.addToBackStack(null);
		transaction.commit(); // Inicio del Fragment Datos de usuario
	}
	
		@Override
	   public void registrarUsuario(String userBd,String passBd){
			this.userBd=userBd;
			this.passBd=passBd;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			
			transaction.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,
					R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
			
			transaction.hide(fragmentRegistrar).replace(R.id.framelayout_contenedor_detalle, fragmento);
			transaction.addToBackStack(null);
			transaction.commit(); // Inicio del Fragment Datos de usuario

			

		}
		
		
		

		
		
}
