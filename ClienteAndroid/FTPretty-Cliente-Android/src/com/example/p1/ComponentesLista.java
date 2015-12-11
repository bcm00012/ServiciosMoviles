package com.example.p1;

import android.os.Parcel;
import android.os.Parcelable;

public class ComponentesLista implements Parcelable {

	private String titulo;
	private int imagen;

	public ComponentesLista(String tit, int imagen) {
		this.titulo = tit;
		this.imagen = imagen;
	}

	public int getImagen() {
		return this.imagen;
	}

	public String getTitulo() {
		return titulo;
	}

	protected ComponentesLista(Parcel in) {
		titulo = in.readString();
		imagen = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(titulo);
		dest.writeInt(imagen);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ComponentesLista> CREATOR = new Parcelable.Creator<ComponentesLista>() {
		@Override
		public ComponentesLista createFromParcel(Parcel in) {
			return new ComponentesLista(in);
		}

		@Override
		public ComponentesLista[] newArray(int size) {
			return new ComponentesLista[size];
		}
	};
}