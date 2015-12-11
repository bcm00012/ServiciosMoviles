package com.example.p1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.p1.Activity_Explorer.AdaptadorTitulares_Menudraw;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;
import com.example.p1.Fragment_Lista_Pc.Interface;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Fragment_Lista_Movil extends Fragment {
	private BroadcastReceiver mReceiver;
	private TextView lblEtiqueta;
	private ListView lstOpciones_movil;
	String opcionSeleccionada;
	static Context context;
	AdaptadorTitulares_movil adaptador_movil;
	static ArrayList<ComponentesLista> datos_movil = new ArrayList<ComponentesLista>();
	ArrayList<ComponentesListaDrawMenu> datos_menudraw;
	AdaptadorTitulares_Menudraw adaptador_menudraw;
	DatosUsuario datosUser;
	View header;

	 /*Metodo Constructor*/
	public Fragment_Lista_Movil() {

	}

	
	/*
	 * Interfaz para devolver Objeto propiedades de esta clase a
	 * Fragment_Lista_Pc
	 */
	public PropiedadesFragment propiedadesFragment() {

		PropiedadesFragment propiedades = new PropiedadesFragment(context, lstOpciones_movil, adaptador_movil,
				datos_movil);
		return propiedades;
	}

	InterfaceMovil mCallback2;

	public interface InterfaceMovil {
		public PropiedadesFragmentPc getPropiedadesFragmentPc();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback2 = (InterfaceMovil) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement TextClicked");
		}
	}

	@Override
	public void onDetach() {
		mCallback2 = null; // => avoid leaking, thanks @Deepscorn
		super.onDetach();
	}

	
	/*
	 * Guarda los datos de usuario ante la destruccion de un activity.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) { 
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putParcelable("adaptador_movil", adaptador_movil);
		outState.putParcelableArrayList("datos_movil", datos_movil);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View rootView = inflater.inflate(R.layout.lista_movil, container,
				false); /*
						 * Metodo inflate para construir y añadir las Views de
						 * layout_fragment_datos_usuario a fragment dinamico en
						 * este caso.
						 */
		
		lstOpciones_movil = (ListView) rootView.findViewById(R.id.LstOpciones);
		header = inflater.inflate(R.layout.header_movil, null);
		lstOpciones_movil.addHeaderView(header);


		/*
		 * Si se han guardado datos por la destruccion del Fragment entra en
		 * esta condicion para recuperar los datos.
		 */
		if (savedInstanceState != null) 
		{
			adaptador_movil = (AdaptadorTitulares_movil) savedInstanceState.getParcelable("adaptador_movil");
			datos_movil = savedInstanceState.getParcelableArrayList("datos_movil");

			datos_menudraw = ((Activity_Explorer) getActivity()).getDatos_menudraw();
			adaptador_menudraw = ((Activity_Explorer) getActivity()).getAdaptador_menudraw();
			datosUser = ((Activity_Explorer) getActivity()).getDatosUser();
			lstOpciones_movil.setAdapter(adaptador_movil);
			registerForContextMenu(lstOpciones_movil);

		} else {

			adaptador_movil = new AdaptadorTitulares_movil(getActivity(), datos_movil);

			datos_menudraw = ((Activity_Explorer) getActivity()).getDatos_menudraw();
			adaptador_menudraw = ((Activity_Explorer) getActivity()).getAdaptador_menudraw();
			datosUser = ((Activity_Explorer) getActivity()).getDatosUser();

			lstOpciones_movil.setAdapter(adaptador_movil);

			new Asynctask_Cliente_List(context, adaptador_movil, datos_movil).execute(); 

			registerForContextMenu(lstOpciones_movil);

		}
		return rootView;
	}

	/* Crea MenuContext */
	@Override
	public void onCreateContextMenu(ContextMenu menun, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menun, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.submenu_movil, menun);
	}

	/*
	 * Captura la pulsacion sobre el MenuContext
	 */

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// mCallback.onEntrada(item); //Recoge el evento de boton y hacemos
		// correr el metodo en el MainActivity con la interfaz

		if (getUserVisibleHint()) {

			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			
			switch (item.getItemId()) {

			case R.id.cnt_mnu_subida_movil:

				opcionSeleccionada = datos_movil.get(info.position - 1).getTitulo();
				Intent intent = new Intent(getActivity(), ServicioSubida.class);
				intent.putExtra("nombreArchivo", opcionSeleccionada);
				intent.putExtra("datosUser", datosUser);

				getActivity().startService(intent);
				break;

			case R.id.cnt_mnu_delete_movil:
				opcionSeleccionada = datos_movil.get(info.position - 1).getTitulo();

				new Asynctask_DeleAndList_Movil(adaptador_menudraw, datos_menudraw, datosUser, context, adaptador_movil,
						datos_movil, opcionSeleccionada).execute(); 
				
				Toast.makeText(context, "Borrando: " + opcionSeleccionada, Toast.LENGTH_SHORT).show();
				
				break;
			case R.id.cnt_mnu_share_movil:

				opcionSeleccionada = datos_movil.get(info.position - 1).getTitulo();

				File pathh = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						"FTPPRETY");	
			
				File filee = new File(pathh + "/" + opcionSeleccionada);			

				Intent sharingIntent = new Intent();
				sharingIntent.setAction(Intent.ACTION_SEND);
				sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filee));

			
				if (opcionSeleccionada.contains(".txt")) {					
					sharingIntent.setType("text/*");
				} else if (opcionSeleccionada.contains(".docx") || (opcionSeleccionada.contains(".doc"))) {
					sharingIntent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				} else if (opcionSeleccionada.contains(".jpg") || (opcionSeleccionada.contains(".png"))
						|| (opcionSeleccionada.contains(".bmp")) || ((opcionSeleccionada.contains(".gif")))) {
					sharingIntent.setType("image/*");		
				} else if (opcionSeleccionada.contains(".pdf")) {
					sharingIntent.setType("application/pdf");
				} else if (opcionSeleccionada.contains(".mp3") || (opcionSeleccionada.contains(".wma"))
						|| (opcionSeleccionada.contains(".wav"))) {
					sharingIntent.setType("audio/*");
				} else if (opcionSeleccionada.contains(".mp4") || (opcionSeleccionada.contains(".avi"))) {
					sharingIntent.setType("video/*");
				} else if (opcionSeleccionada.contains(".zip")) {
					sharingIntent.setType("application/zip");
				} else if (opcionSeleccionada.contains(".rar")) {
					sharingIntent.setType("application/x-rar-compressed");
			
				} else if (opcionSeleccionada.contains(".")) {
					sharingIntent.setType("application/*");
				}
				
				startActivity(sharingIntent);
				
				break;
			case R.id.cnt_mnu_abrir:

				opcionSeleccionada = datos_movil.get(info.position - 1).getTitulo();
				Intent i = new Intent();
				i.setAction(android.content.Intent.ACTION_VIEW);

				File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						"FTPPRETY");

				File file = new File(path + "/" + opcionSeleccionada);

				if (opcionSeleccionada.contains(".txt")) {
					i.setDataAndType(Uri.fromFile(file), "text/*");
				} else if (opcionSeleccionada.contains(".docx") || (opcionSeleccionada.contains(".doc"))) {
					i.setDataAndType(Uri.fromFile(file), "application/*");
				} else if (opcionSeleccionada.contains(".jpg") || (opcionSeleccionada.contains(".png"))
						|| (opcionSeleccionada.contains(".bmp")) || ((opcionSeleccionada.contains(".gif")))) {
					i.setDataAndType(Uri.fromFile(file), "image/*");
				} else if (opcionSeleccionada.contains(".pdf")) {
					i.setDataAndType(Uri.fromFile(file), "application/*");
				} else if (opcionSeleccionada.contains(".mp3") || (opcionSeleccionada.contains(".wma"))
						|| (opcionSeleccionada.contains(".wav"))) {
					i.setDataAndType(Uri.fromFile(file), "audio/*");
				} else if (opcionSeleccionada.contains(".mp4") || (opcionSeleccionada.contains(".avi"))) {
					i.setDataAndType(Uri.fromFile(file), "video/*");
				} else if (opcionSeleccionada.contains(".zip")) {
					i.setDataAndType(Uri.fromFile(file), "application/zip");
				} else if (opcionSeleccionada.contains(".rar")) {
					i.setDataAndType(Uri.fromFile(file), "application/x-rar-compressed");

				} else if (opcionSeleccionada.contains(".")) {
					i.setDataAndType(Uri.fromFile(file), "application/*");
				}

				startActivity(i);

				break;

			}

			return true;
		}
		return true;
	}

	public static class AdaptadorTitulares_movil extends ArrayAdapter<ComponentesLista>implements Parcelable {

		protected AdaptadorTitulares_movil(Parcel in) {
			super(context, R.layout.contenido_lista_pc, datos_movil);
		}

		AdaptadorTitulares_movil(Context context, ArrayList<ComponentesLista> datos_movil) {
			super(context, R.layout.contenido_lista_movil, datos_movil);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View item = convertView;
			ViewHolder holder;

			if (item == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				item = inflater.inflate(R.layout.contenido_lista_movil, null);

				holder = new ViewHolder();
				holder.titulo = (TextView) item.findViewById(R.id.LblTitulo);
				holder.image = (ImageView) item.findViewById(R.id.imagen);
				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			holder.titulo.setText(datos_movil.get(position).getTitulo());

			holder.image
					.setImageDrawable(getContext().getResources().getDrawable(datos_movil.get(position).getImagen()));
			return (item);
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<AdaptadorTitulares_movil> CREATOR = new Parcelable.Creator<AdaptadorTitulares_movil>() {
			@Override
			public AdaptadorTitulares_movil createFromParcel(Parcel in) {
				return new AdaptadorTitulares_movil(in);
			}

			@Override
			public AdaptadorTitulares_movil[] newArray(int size) {
				return new AdaptadorTitulares_movil[size];
			}
		};

	}

	static class ViewHolder {
		TextView titulo;
		ImageView image;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// extract our message from intent

				String msg_for_me = intent.getStringExtra("some_msg");
				Log.v("Fragment", "Fragment_Lista_Movil LO QUE RECIBOOOOOOOOOOOOOOOOOOOOOOOO " + msg_for_me);
				// log our message value
				Log.v("conexion", msg_for_me);
				if (msg_for_me.equalsIgnoreCase("final2")) {
					Log.v("Fragment", "Fragment_Lista_Movil ENTRA EN MI CONDICIONNNNN");

					getActivity().stopService(intent);

					Log.v("Fragment", "Fragment_Lista_Movil SERVICIO DE SUBIDA ACTIVO?" + isMyServiceRunning(ServicioSubida.class));
					PropiedadesFragmentPc pro = mCallback2.getPropiedadesFragmentPc();

					new Asynctask_List(context, pro.adaptador_pc, pro.getDatos_pc()).execute(); 

					UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(context);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					DateTime dt = new DateTime();
					db.execSQL("INSERT INTO " + datosUser.getUser()
							+ "(nomb_op,estado_op,fecha_op,hora_op) VALUES('Subida','OK'," + "'" + dt.getCurrentDate()
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

					new Asynctask_QueryBD_ListHistory(datosUser.getUser(), context, adaptador_menudraw, datos_menudraw)
							.execute(); 
				}

			}
		};
		
		getActivity().registerReceiver(mReceiver, intentFilter);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// unregister our receiver
		getActivity().unregisterReceiver(this.mReceiver);
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
}
