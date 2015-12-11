package com.example.p1;

import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class Fragment_DatosUsuario extends Fragment implements OnClickListener {
	InterfazDatosUser mCallback;
	 Button mButton;
	 public EditText user,pass,ip,puerto;

//////////////////////////////////////////////////////// Constructor ////////////////////////////////////////////////////////

	public void Fragment_Detalle(){		//Clase Fragment para el fragmento dinamico en el que se introducen los datos de usuario
	}
	
	
////////////////////////////////////////////////////////Interfaz Para el evento de boton con MainActivity ////////////////////////////////////////////////////////

	public interface InterfazDatosUser {
	    public void conectarServidor(DatosUsuario datosUser);
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    
	    // This makes sure that the container activity has implemented
	    // the callback interface. If not, it throws an exception
	    try {
	        mCallback = (InterfazDatosUser) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString()
	                + " must implement OnHeadlineSelectedListener");
	    }
	}
	
	
////////////////////////////////////////////////////////onCreateView del Fragment ////////////////////////////////////////////////////////

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_fragment_datos_usuario, container, false); //Metodo inflate para construir y añadir las Views de layout_fragment_datos_usuario a fragment dinamico en este caso.

		//Mostramos el contenido al usuario
		mButton = (Button) rootView.findViewById(R.id.button_uno);	// Id para el boton del fragment que inicia la activida dos
		mButton.setOnClickListener(this);	
		user   = (EditText)rootView.findViewById(R.id.user);	//Id del nombre de usuario del layout
		pass   = (EditText)rootView.findViewById(R.id.pass);	//Id la password de usuario del layout
		ip   = (EditText)rootView.findViewById(R.id.ip);		//Id de la ip en la que se encuentra el servidor
		puerto   = (EditText)rootView.findViewById(R.id.puerto);//Id del puerto en el que escucha el servidor
		
		if (savedInstanceState != null)	//Si se han guardado datos por la destruccion del Activity entra en esta condicion para recuperar los datos.
	      {
			 	//Son recuperados los datos de usuario.
			 	String edit1 = savedInstanceState.getString("userSave");
		        String edit2 = savedInstanceState.getString("passSave");
		        String edit3 = savedInstanceState.getString("ipSave");
		        String edit4 = savedInstanceState.getString("puertoSave");
		        //Se insertan los datos en los campos del layout.
		        user.setText(edit1);
		        pass.setText(edit2);
		        ip.setText(edit3);
		        puerto.setText(edit4);
	      }
		return rootView;
	}

	
////////////////////////////////////////////////////////Guarda estado del Fragment ante una destruccion ////////////////////////////////////////////////////////
	 @Override
	public void onSaveInstanceState(Bundle outState) {	//Guarda los datos de usuario ante la destruccion de un activity.
	        // TODO Auto-generated method stub
	        super.onSaveInstanceState(outState);
	        
	        String userSave = user.getText().toString(); 
	        String passSave = pass.getText().toString();
	        String ipSave = ip.getText().toString();
	        String puertoSave =  puerto.getText().toString();
	         
	        outState.putString("userSave", userSave);
	        outState.putString("passSave", passSave);
	        outState.putString("ipSave", ipSave);
	        outState.putString("puertoSave", puertoSave);     
	    }
	
////////////////////////////////////////////////////////  Evento onClick  ////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {	//Recoge el evento cuando el boton del fragment es pulsado.
	 //do what you want to do when button is clicked
		String port = this.puerto.getText().toString();
		short puerto;
		if(port.equalsIgnoreCase("")){
			puerto = 21;
		}else{
			puerto = Short.parseShort(this.puerto.getText().toString());
		}
		DatosUsuario datosUser= new DatosUsuario(user.getText().toString(),pass.getText().toString(),ip.getText().toString(),puerto);
        mCallback.conectarServidor(datosUser);
	    }
	}
	
	
	
