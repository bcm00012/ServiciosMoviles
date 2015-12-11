package com.example.p1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Carrusel extends Fragment {

	public void Fragment_Estatico() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.layout_fragment_carrusel, container, false);/* Metodo inflate para construir
																					 y añadir  las  Views  de layout_fragment_carrusel
																					 a fragment estatico en este caso.
																					 */
	}
}
