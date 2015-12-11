package com.example.p1;



import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class FragmentEmpezar extends Fragment implements OnClickListener {

	InterfazEmpezar mCallback;
	Button mButton;
	Button mButtonRegistrar;
	
	
////////////////////////////////////////////////////////  Interfaz Para el evento de boton con MainActivity ////////////////////////////////////////////////////////

	public interface InterfazEmpezar {	//Interfaz con la es comunicada el evento del boton comunicados eventos
	    public void onEntrada();	//Metodo que es iniciado en MainActivity
	    public void onRegistrar();
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    
	    // This makes sure that the container activity has implemented
	    // the callback interface. If not, it throws an exception
	    try {
	        mCallback = (InterfazEmpezar) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString()
	                + " must implement OnHeadlineSelectedListener");
	    }
	}



////////////////////////////////////////////////////////onCreateView del Dragment ////////////////////////////////////////////////////////
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_fragment_boton_empezar, container, false);	//Metodo inflate para construir y añadir las Views de layout_fragment_boton_empezar a fragment dinamico en este caso.
		
		mButton = (Button) rootView.findViewById(R.id.button_tres);	//Id del boton del layout
		mButton.setOnClickListener(this);	
		mButtonRegistrar = (Button) rootView.findViewById(R.id.button_registrar);	//Id del boton del layout
		mButtonRegistrar.setOnClickListener(this);
		return rootView;
	}

	
	
////////////////////////////////////////////////////////  Ejecuta La interfaz con MainActivity	onEntrada() ////////////////////////////////////////////////////////

	@Override
	public void onClick(View v ) {	//Recoge el evento del boton del fragment
	 //do what you want to do when button is clicked
		
		 if (v.getId() == R.id.button_tres){
			 mCallback.onEntrada();
		 }else if (v.getId() == R.id.button_registrar){
			 mCallback.onRegistrar();
		 }
		
        	//Recoge el evento de boton y hacemos correr el metodo en el MainActivity con la interfaz
        //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }



	}
	
	
	

