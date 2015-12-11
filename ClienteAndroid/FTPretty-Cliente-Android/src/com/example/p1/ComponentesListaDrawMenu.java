package com.example.p1;

public class ComponentesListaDrawMenu {

	private String nombre, hora, fecha, id;
	private int imagen;

	public ComponentesListaDrawMenu(String id, String nombre, String fecha, String hora, int imagen) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.id = id;
		this.imagen = imagen;

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

}
