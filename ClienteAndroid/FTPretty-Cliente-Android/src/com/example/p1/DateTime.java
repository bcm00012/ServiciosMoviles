package com.example.p1;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTime {

	String currentDate = "";
	String currentHour = "";

	public DateTime() {
		Calendar fecha = new GregorianCalendar();

		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH);
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		int segundo = fecha.get(Calendar.SECOND);

		String hour = String.format("%02d", hora);
		String minutes = String.format("%02d", minuto);
		String seconds = String.format("%02d", segundo);

		currentDate = dia + "/" + (mes + 1) + "/" + anio;
		currentHour = hour + ":" + minutes + ":" + seconds;

	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getCurrentHour() {
		return currentHour;
	}

	public void setCurrentHour(String currentHour) {
		this.currentHour = currentHour;
	}

}
