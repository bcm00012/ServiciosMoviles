package com.example.p1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.example.p1.Activity_Explorer.AdaptadorTitulares_Menudraw;
import com.example.p1.Fragment_Lista_Movil.AdaptadorTitulares_movil;
import com.example.p1.Fragment_Lista_Movil.ViewHolder;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Lista_Pc extends Fragment {// implements
													// Fragment_Lista_Movil.OnHeadlineSelectedListener{

	private ListView lstOpciones_pc;
	ImageView imagen_pc;
	static ArrayList<ComponentesLista> datos_pc = new ArrayList<ComponentesLista>();
	String destinationFolder = "";
	String carpetaAnterior = "..";
	DatosUsuario datosUser;
	String opcionSeleccionada;
	Sokect_Tcp s = Sokect_Tcp.getInstance();
	PrintWriter out = null;
	AdaptadorTitulares_pc adaptador_pc;
	static Context context;
	ArrayList<ComponentesLista> datos_movil;
	private BroadcastReceiver mReceiver;
	Interface mCallback;
	Context contentToast ;
	ArrayList<ComponentesListaDrawMenu> datos_menudraw;
	AdaptadorTitulares_Menudraw adaptador_menudraw;



	public Fragment_Lista_Pc() {
	}

	/*
	 * Guarda los datos de usuario ante la destruccion de un activity.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) { 
		
		super.onSaveInstanceState(outState);
		outState.putParcelable("adaptador_pc", adaptador_pc); 
		outState.putParcelableArrayList("datos_pc", datos_pc);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		
		contentToast =  getActivity();
		
		datosUser = (DatosUsuario) bundle.getSerializable("datosUser");
		context = getActivity().getApplicationContext();		

		View rootView_pc = inflater.inflate(R.layout.lista_pc, container,false); 
						/*
						 * Metodo inflate para construir y añadir // las Views
						 * de layout_fragment_datos_usuario a fragment dinamico
						 * en este caso.
						 */
		Log.v("Conexion", "Fragment lista");
		lstOpciones_pc = (ListView) rootView_pc.findViewById(R.id.LstOpciones_pc);
		Log.v("Conexion", "Fragment lista");

		View header_pc = inflater.inflate(R.layout.header_pc, null);
		lstOpciones_pc.addHeaderView(header_pc);
		Log.v("Conexion", "Justo antes");
		
		/*
		 * Si se han guardado datos por la
		 * destruccion del Activity entra en
		 * esta condicion para recuperar los
		 * datos. savedInstanceState
		 */
		if (savedInstanceState != null) 
		{
			// Son recuperados los datos de usuario.
			adaptador_pc = (AdaptadorTitulares_pc) savedInstanceState.getParcelable("adaptador_pc");
			datos_pc = savedInstanceState.getParcelableArrayList("datos_pc");
			lstOpciones_pc.setAdapter(adaptador_pc);

			datos_menudraw = ((Activity_Explorer) getActivity()).getDatos_menudraw();
			adaptador_menudraw = ((Activity_Explorer) getActivity()).getAdaptador_menudraw();

			registerForContextMenu(lstOpciones_pc);

		} else {

			adaptador_pc = new AdaptadorTitulares_pc(context, datos_pc);
			Log.v("Conexion", "Justo antes 2");
			lstOpciones_pc.setAdapter(adaptador_pc);

			datos_menudraw = ((Activity_Explorer) getActivity()).getDatos_menudraw();
			adaptador_menudraw = ((Activity_Explorer) getActivity()).getAdaptador_menudraw();

			new Asynctask_List(context, adaptador_pc, datos_pc).execute(); 
			
			registerForContextMenu(lstOpciones_pc);

		}
		lstOpciones_pc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Log.v("Conexion", "Entra en el evento de boton para hacer el CWD FRAGEMT LISTA PC");
				try {
					String opcionSeleccionada = ((ComponentesLista) a.getItemAtPosition(position)).getTitulo();

					if (opcionSeleccionada.contains("Atras")) {

						Log.v("Conexion", "Entra en el boton apartado ATRAS " + opcionSeleccionada);
						new Asynctask_CwdAndList(context, contentToast, adaptador_pc, datos_pc, carpetaAnterior)
								.execute();

					} else if (opcionSeleccionada.contains(".") == false) {
						Log.v("Conexion", "Entra en el boton apartado ALANTE " + opcionSeleccionada);

						new Asynctask_CwdAndList(context, contentToast, adaptador_pc, datos_pc, opcionSeleccionada)
								.execute();

					} else {

						Log.v("Conexion", "Entra en el boton apartado ESLE " + opcionSeleccionada);

					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("Fragment", "Fragment_Lista_Pc setOnItemClickListener");

				}
			}
		});

		return rootView_pc;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.submenu_pc, menu);
	}


	public boolean onContextItemSelected(android.view.MenuItem item) {
		Log.v("menu", "onContex PC 1");

		if (getUserVisibleHint()) {

			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Log.v("menu", "onContex PC 2");
			switch (item.getItemId()) {
			case R.id.cnt_mnu_desc:
				Log.v("Conexion", "Entra en el boton apartado edit");

				opcionSeleccionada = datos_pc.get(info.position - 1).getTitulo();

				if (opcionSeleccionada.contains(".")) {
					Log.v("Conexion", opcionSeleccionada);

					PropiedadesFragment pro = mCallback.getPropiedadesFragmentMovil();

					Intent intent = new Intent(getActivity(), ServicioDescarga.class);
					intent.putExtra("nombreArchivo", opcionSeleccionada);
					intent.putExtra("myData", pro.getAdaptador()); 
					intent.putParcelableArrayListExtra("myData2", pro.getArray());
					intent.putExtra("datosUser", datosUser);
					getActivity().startService(intent);
					
					Toast.makeText(context, "Descargando: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();
					
				} else {
		
					Toast.makeText(context, "No se puede descargar una carpeta", Toast.LENGTH_SHORT).show();
				}
				
				break;

			case R.id.cnt_mnu_delete:
				opcionSeleccionada = datos_pc.get(info.position - 1).getTitulo();

				new Asynctask_DeleAndList(adaptador_menudraw, datos_menudraw, datosUser.getUser(), context,contentToast,
						adaptador_pc, lstOpciones_pc, datos_pc, opcionSeleccionada).execute();
				
				Toast.makeText(context, "Borrando: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();
				
				break;

			}
			return false;
		}


		return false;
	}

	/*
	 * Interfaz para devolver Objeto propiedades de esta clase a
	 * Fragment_Lista_Pc
	 */

	public PropiedadesFragmentPc propiedadesFragmentPc() {

		PropiedadesFragmentPc propiedades = new PropiedadesFragmentPc(adaptador_pc, datos_pc);
		return propiedades;
	}

	public interface Interface {
		public PropiedadesFragment getPropiedadesFragmentMovil();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (Interface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement TextClicked");
		}
	}

	@Override
	public void onDetach() {
		mCallback = null; // => avoid leaking, thanks @Deepscorn
		super.onDetach();
	}

	
	
	/* Clase AdaptadorTitulares_pc, Clase Adaptador para la lista de archivos */
	
	public static class AdaptadorTitulares_pc extends ArrayAdapter<ComponentesLista>implements Parcelable {

		protected AdaptadorTitulares_pc(Parcel in) {
			super(context, R.layout.contenido_lista_pc, datos_pc);
		}

		AdaptadorTitulares_pc(Context context, ArrayList<ComponentesLista> datos_pc) {
			super(context, R.layout.contenido_lista_pc, datos_pc);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View item_pc = convertView;
			ViewHolder holder_pc;

			if (item_pc == null) {
				LayoutInflater inflater_pc = LayoutInflater.from(getContext());
				item_pc = inflater_pc.inflate(R.layout.contenido_lista_pc, null);

				holder_pc = new ViewHolder();
				holder_pc.titulo_pc = (TextView) item_pc.findViewById(R.id.LblTitulo_pc);

				holder_pc.image_pc = (ImageView) item_pc.findViewById(R.id.imagen_pc);
				item_pc.setTag(holder_pc);
			} else {
				holder_pc = (ViewHolder) item_pc.getTag();
			}

			holder_pc.titulo_pc.setText(datos_pc.get(position).getTitulo());

			holder_pc.image_pc
					.setImageDrawable(getContext().getResources().getDrawable(datos_pc.get(position).getImagen()));
			return (item_pc);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {

		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<AdaptadorTitulares_pc> CREATOR = new Parcelable.Creator<AdaptadorTitulares_pc>() {
			@Override
			public AdaptadorTitulares_pc createFromParcel(Parcel in) {
				return new AdaptadorTitulares_pc(in);
			}

			@Override
			public AdaptadorTitulares_pc[] newArray(int size) {
				return new AdaptadorTitulares_pc[size];
			}
		};
	}

	static class ViewHolder {
		TextView titulo_pc;
		TextView subtitulo_pc;
		ImageView image_pc;
	}

	
	@Override
	public void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				String msg_for_me = intent.getStringExtra("some_msg");
				Log.v("Fragment", "Fragment_Lista_Pc, onResume, Descarga: " + msg_for_me);
				if (msg_for_me.equalsIgnoreCase("final")) {
					Log.v("Fragment", "Fragment_Lista_Pc, onResume, Entra en el if" );
					PropiedadesFragment pro = mCallback.getPropiedadesFragmentMovil();
					getActivity().stopService(intent);

					Log.v("Fragment", "Fragment_Lista_Pc, SERVICIO DE DESCARGA ACTIVO?" + isMyServiceRunning(ServicioDescarga.class));

					new Asynctask_Cliente_List(context, pro.getAdaptador(), pro.getArray()).execute(); 
					
					UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					DateTime dt = new DateTime();
					db.execSQL("INSERT INTO " + datosUser.getUser()
							+ "(nomb_op,estado_op,fecha_op,hora_op) VALUES('Descarga','OK'," + "'" + dt.getCurrentDate()
							+ "'," + "'" + dt.getCurrentHour() + "')");
					String id, nomb, estado, fecha, hora;
					Cursor c = db.rawQuery(" SELECT * FROM " + datosUser.getUser(), null);
					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros
						do {
							id = c.getString(0);
							nomb = c.getString(1);
							estado = c.getString(2);
							fecha = c.getString(3);
							hora = c.getString(4);

							Log.v("Fragment", id);
							Log.v("Fragment", nomb);
							Log.v("Fragment", estado);
							Log.v("Fragment", fecha);
							Log.v("Fragment", hora);

						} while (c.moveToNext());
					}

					new Asynctask_QueryBD_ListHistory(datosUser.getUser(), context, adaptador_menudraw, datos_menudraw).execute();

				}

			}
		};
		
		getActivity().registerReceiver(mReceiver, intentFilter);
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// unregister our receiver
		getActivity().unregisterReceiver(this.mReceiver);
	}

}
