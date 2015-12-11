package com.example.p1;

import java.util.ArrayList;

import com.example.p1.Fragment_Lista_Movil.ViewHolder;
import com.example.p1.Fragment_Lista_Pc.AdaptadorTitulares_pc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Activity_Explorer extends Activity implements Fragment_Lista_Pc.Interface, Fragment_Lista_Movil.InterfaceMovil{
    int salida = 0;
    boolean historialOn = false;
	   DatosUsuario datosUser;
	   Sokect_Tcp s;
	   String[] menu;
	   Fragment fragmento_movil = new Fragment_Lista_Movil( );
	   Fragment fragmento_pc = new Fragment_Lista_Pc();
	   Context context;
		private TextView lblEtiqueta_pc;
	     DrawerLayout dLayout;
	     ListView lista_menudraw;
	     ArrayList <ComponentesListaDrawMenu> datos_menudraw = new ArrayList <ComponentesListaDrawMenu>();
	     AdaptadorTitulares_Menudraw adaptador_menudraw;
		


	     @Override
	     public PropiedadesFragment getPropiedadesFragmentMovil(){
	         // Get Fragment B
	       Fragment_Lista_Movil frag = (Fragment_Lista_Movil)getFragmentManager().findFragmentById(R.id.framelayout_contenedor_detalle);
	         return frag.propiedadesFragment();
	     }
	     @Override
	     public PropiedadesFragmentPc getPropiedadesFragmentPc(){
	         // Get Fragment A
	       Fragment_Lista_Pc frag2 = (Fragment_Lista_Pc)getFragmentManager().findFragmentById(R.id.framelayout_contenedor_detalle_pc);
	         return frag2.propiedadesFragmentPc();
	     }
	
	     
	 	@Override
		protected void onSaveInstanceState(Bundle outState) {	//Metodo que Guarda los datos ante la destruccion de un Activity
		    super.onSaveInstanceState(outState);
		    getFragmentManager().putFragment(outState, "fragmento_pc",fragmento_pc);
		    getFragmentManager().putFragment(outState, "fragmento_movil",fragmento_movil);	//Guarda el estado del fragmento para recuperarlo con el nombre "fragmento" posteriormente

	 	}
	     

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_listas);
		
		context = getApplicationContext();
		datosUser = (DatosUsuario)getIntent().getExtras().getSerializable("datosUser");
		
		menu = new String[]{"Home","Android","Windows","Linux","Raspberry Pi","WordPress","Videos","Contact Us"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lista_menudraw = (ListView) findViewById(R.id.left_drawer);
//		View header_draw_menu = inflater.inflate(R.layout.header_draw_menu, null);
//		lista_menudraw.addHeaderView(header_draw_menu);
		View header_draw_menu = LayoutInflater.from(this).inflate(R.layout.header_draw_menu, null);
		lista_menudraw.addHeaderView(header_draw_menu);
        adaptador_menudraw = new AdaptadorTitulares_Menudraw(getApplicationContext(), datos_menudraw);
        
        lista_menudraw.setAdapter(adaptador_menudraw);
        lista_menudraw.setSelector(android.R.color.holo_blue_dark);
        // metodo para pasar los argumentos de la asyntask del log, a los demás fragment donde se ejecutan las demas asyntask
        
////////////////////////////////////////////////////////////////////////////////////////////    
		 
   	 	new Asynctask_BBDD(datosUser,this).execute(" Estos Strings van a variableNoUsada que no usaremos en este ejemplo y podiamos haber declarado como Void "," si lo necesitaramos podemos cambiar el String por otro tipo de datos "," podemos añadir más de 4 datos que los de este ejemplo, todos los que necesitemos "," y recuerda que se usan como un array, para acceder en concreto a este usaríamos variableNoUsada[3] "); //Arrancamos el AsyncTask. el método "execute" envía datos directamente a doInBackground()
   	 	new Asynctask_QueryBD_ListHistory(datosUser.getUser(),context, adaptador_menudraw,datos_menudraw).execute(" Estos Strings van a variableNoUsada que no usaremos en este ejemplo y podiamos haber declarado como Void "," si lo necesitaramos podemos cambiar el String por otro tipo de datos "," podemos añadir más de 4 datos que los de este ejemplo, todos los que necesitemos "," y recuerda que se usan como un array, para acceder en concreto a este usaríamos variableNoUsada[3] "); //Arrancamos el AsyncTask. el método "execute" envía datos directamente a doInBackground()
   	 		
	     	//Objeto Fragment Datos de ususario
        if (savedInstanceState != null) {	//Recupera el Franment de Datos de usuario ante la destruccion de un Activity

			FragmentTransaction transaction_pc = getFragmentManager().beginTransaction();
		    fragmento_pc = getFragmentManager().getFragment(savedInstanceState, "fragmento_pc");	//Recupera el Fragmenta
		    
			FragmentTransaction transaction_movil = getFragmentManager().beginTransaction();
		    fragmento_movil = getFragmentManager().getFragment(savedInstanceState, "fragmento_movil");	//Recupera el Fragmenta

		//  transaction_movil.replace(R.id.framelayout_contenedor_detalle, fragmento_movil).commit();
		//	transaction_pc.replace(R.id.framelayout_contenedor_detalle_pc, fragmento_pc).commit();	//Inicia el Fragment de entrada a la app

		 }else{
		     FragmentTransaction transaction_pc = getFragmentManager().beginTransaction();
		     Bundle args = new Bundle();
		     args.putSerializable("datosUser", datosUser);
		     fragmento_pc.setArguments(args);
			 transaction_pc.add(R.id.framelayout_contenedor_detalle_pc, fragmento_pc).commit();	//Inicia el Fragment de entrada a la app
		     
		     FragmentTransaction transaction_movil = getFragmentManager().beginTransaction();
			 transaction_movil.add(R.id.framelayout_contenedor_detalle, fragmento_movil).commit();	//Inicia el Fragment de entrada a la app
		 }
	}

	public AdaptadorTitulares_Menudraw getAdaptador_menudraw() {
		return adaptador_menudraw;
	}

	public void setAdaptador_menudraw(AdaptadorTitulares_Menudraw adaptador_menudraw) {
		this.adaptador_menudraw = adaptador_menudraw;
	}
	
	public ArrayList<ComponentesListaDrawMenu> getDatos_menudraw() {
		return datos_menudraw;
	}

	public void setDatos_menudraw(ArrayList<ComponentesListaDrawMenu> datos_menudraw) {
		this.datos_menudraw = datos_menudraw;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__explorer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_disc) {

//			TextView headerView = (TextView) LayoutInflater.from(this).inflate(R.layout.header_draw_menu, null);
//			headerView.setText("");

			UsuariosSQLiteHelper dbHelper = new UsuariosSQLiteHelper(getApplicationContext()); 
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        DateTime dt = new DateTime ();
	        db.execSQL("INSERT INTO "+datosUser.getUser()+"(nomb_op,estado_op,fecha_op,hora_op) VALUES('Desconexion','OK'," +"'"+dt.getCurrentDate()+"',"+"'"+dt.getCurrentHour()+"')");
	        String iden,nomb,estado,fecha,hora;
	        Cursor c = db.rawQuery(" SELECT * FROM "+datosUser.getUser(), null);

	    	new Asynctask_QueryBD_ListHistory(datosUser.getUser(),getApplicationContext(), adaptador_menudraw,datos_menudraw).execute(" Estos Strings van a variableNoUsada que no usaremos en este ejemplo y podiamos haber declarado como Void "," si lo necesitaramos podemos cambiar el String por otro tipo de datos "," podemos añadir más de 4 datos que los de este ejemplo, todos los que necesitemos "," y recuerda que se usan como un array, para acceder en concreto a este usaríamos variableNoUsada[3] "); //Arrancamos el AsyncTask. el método "execute" envía datos directamente a doInBackground()

	    	db.execSQL("DELETE FROM AUTENTICA WHERE id=1");
	    	
	    	new AsyncTask_Quit(context, Activity_Explorer.this).execute();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);			
			
			return true;
		}
		
		if (id == R.id.menu_history) {
			dLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

			if (historialOn == false) {
				dLayout.openDrawer(lista_menudraw);// (R.id.left_drawer);
				historialOn = true;
			} else {
				dLayout.closeDrawer(lista_menudraw);// (R.id.left_drawer);
				historialOn = false;
			}
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch(keyCode){
				
			case KeyEvent.KEYCODE_BACK:
				salida = salida+1;
				if (salida>1){
					
					new AsyncTask_Quit(context,Activity_Explorer.this).execute();
					return true;
				}
				
				else{
				Toast.makeText(this, "Pulsa otra vez para salir",
                                                        Toast.LENGTH_SHORT).show();
				}
				return true;
		}
		
		
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	  public DatosUsuario getDatosUser() {
		return datosUser;
	}

	public void setDatosUser(DatosUsuario datosUser) {
		this.datosUser = datosUser;
	}


	class AdaptadorTitulares_Menudraw extends ArrayAdapter<ComponentesListaDrawMenu> {

		  AdaptadorTitulares_Menudraw(Context context, ArrayList <ComponentesListaDrawMenu>  datos) {
	            super(context, R.layout.contenido_lista_draw_menu, datos);
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {

	            View item = convertView;
	            ViewHolder holder;

	            if(item == null)
	            {
	                LayoutInflater inflater = LayoutInflater.from(getContext());
	                item = inflater.inflate(R.layout.contenido_lista_draw_menu, null);

	                holder = new ViewHolder();
	                holder.id = (TextView)item.findViewById(R.id.drawmenu_id);
	                holder.nombre = (TextView)item.findViewById(R.id.drawmenu_nombre);
	                holder.fecha = (TextView)item.findViewById(R.id.drawmenu_fecha);
	                holder.hora = (TextView)item.findViewById(R.id.drawmenu_hora);
	                holder.image = (ImageView) item.findViewById(R.id.imagenstate);
	                item.setTag(holder);
	            }
	            else
	            {
	                holder = (ViewHolder)item.getTag();
	            }
	            holder.id.setText(datos_menudraw.get(position).getId());
	            holder.nombre.setText(datos_menudraw.get(position).getNombre());
	            holder.fecha.setText(datos_menudraw.get(position).getFecha());
	            holder.hora.setText(datos_menudraw.get(position).getHora());

	            holder.image.setImageDrawable(getResources().getDrawable(datos_menudraw.get(position).getImagen()));
	            return(item);
	        }
	    }

	    static class ViewHolder {
	        TextView id;
	        TextView nombre;
	        TextView fecha;
	        TextView hora;
	        ImageView image;
	    }

	
		
	
	
}
