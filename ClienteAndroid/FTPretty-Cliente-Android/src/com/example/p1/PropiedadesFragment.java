package com.example.p1;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;

import android.content.Context;
import android.widget.ListView;

public class PropiedadesFragment implements Serializable {
	
	Context context;
	ListView lstOpciones_movil;
	AdaptadorTitulares_movil adaptador_movil;
	ArrayList<ComponentesLista> datos_movil;

	PropiedadesFragment(Context context, ListView lstOpciones_movil, AdaptadorTitulares_movil adaptador_movil,
			ArrayList<ComponentesLista> datos_movil) {
		this.context = context;
		this.lstOpciones_movil = lstOpciones_movil;
		this.adaptador_movil = adaptador_movil;
		this.datos_movil = datos_movil;
	}

	public Context getContext() {
		return context;
	}

	public ListView getListView() {
		return lstOpciones_movil;
	}

	public AdaptadorTitulares_movil getAdaptador() {
		return adaptador_movil;
	}

	public ArrayList<ComponentesLista> getArray() {
		return datos_movil;
	}

}