package com.example.p1;

import com.example.p1.Fragment_DatosUsuario.InterfazDatosUser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Fragment_RegistrarUsuario extends Fragment implements OnClickListener {

	
	

	InterfazRegistrar mCallback ;
	 Button mButton;
	 public EditText user,pass,ip,puerto;

//////////////////////////////////////////////////////// Constructor ////////////////////////////////////////////////////////

	public void Fragment_RegistrarUsuario(){		//Clase Fragment para el fragmento dinamico en el que se introducen los datos de usuario
	}
	
	
////////////////////////////////////////////////////////Interfaz Para el evento de boton con MainActivity ////////////////////////////////////////////////////////

	public interface InterfazRegistrar {
	    public void registrarUsuario(String usuarioBd,String passBd);
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    
	    // This makes sure that the container activity has implemented
	    // the callback interface. If not, it throws an exception
	    try {
	        mCallback = (InterfazRegistrar) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString()
	                + " must implement OnHeadlineSelectedListener");
	    }
	}
	
	
////////////////////////////////////////////////////////onCreateView del Fragment ////////////////////////////////////////////////////////

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_fragment_registar, container, false); //Metodo inflate para construir y añadir las Views de layout_fragment_datos_usuario a fragment dinamico en este caso.

		//Mostramos el contenido al usuario
		mButton = (Button) rootView.findViewById(R.id.button_uno);	// Id para el boton del fragment que inicia la activida dos
		mButton.setOnClickListener(this);	
		user   = (EditText)rootView.findViewById(R.id.user);	//Id del nombre de usuario del layout
		pass   = (EditText)rootView.findViewById(R.id.pass);	//Id la password de usuario del layout
		
		
		if (savedInstanceState != null)	//Si se han guardado datos por la destruccion del Activity entra en esta condicion para recuperar los datos.
	      {
			 	//Son recuperados los datos de usuario.
			 	String edit1 = savedInstanceState.getString("userSave");
		        String edit2 = savedInstanceState.getString("passSave");
		       
		        //Se insertan los datos en los campos del layout.
		        user.setText(edit1);
		        pass.setText(edit2);

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

	         
	        outState.putString("userSave", userSave);
	        outState.putString("passSave", passSave);
    
	    }
	
////////////////////////////////////////////////////////  Evento onClick  ////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {	//Recoge el evento cuando el boton del fragment es pulsado.
	 //do what you want to do when button is clicked
		 // Paso deparametros al nuevo
											// activity
		mCallback.registrarUsuario(user.getText().toString(),pass.getText().toString());
	    }
	}
	
	
	

	
	
	
	
	
	
	
	
	

