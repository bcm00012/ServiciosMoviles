package com.example.p1;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.content.Context;
import android.widget.ListView;

public class PropiedadesFragmentPc implements Serializable {

	AdaptadorTitulares_pc adaptador_pc;
	ArrayList<ComponentesLista> datos_pc;

	public PropiedadesFragmentPc(AdaptadorTitulares_pc adaptador_pc, ArrayList<ComponentesLista> datos_pc) {
		super();
		this.adaptador_pc = adaptador_pc;
		this.datos_pc = datos_pc;
	}

	public AdaptadorTitulares_pc getAdaptador_pc() {
		return adaptador_pc;
	}

	public void setAdaptador_pc(AdaptadorTitulares_pc adaptador_pc) {
		this.adaptador_pc = adaptador_pc;
	}

	public ArrayList<ComponentesLista> getDatos_pc() {
		return datos_pc;
	}

	public void setDatos_pc(ArrayList<ComponentesLista> datos_pc) {
		this.datos_pc = datos_pc;
	}

}