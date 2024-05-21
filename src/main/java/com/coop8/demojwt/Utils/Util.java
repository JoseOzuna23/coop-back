package com.coop8.demojwt.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Util {

	//private static Gson gson = new Gson();
	private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private DecimalFormat dfn = null;
	private SimpleDateFormat dateFormat = null;

	/**
	 * Método;todo que verifica si un objeto es nulo o vacío
	 *
	 * @param objeto
	 *
	 * @return {@code TRUE} si es nulo o vacio sino {@code FALSE}
	 *
	 * @author jose.acosta 12.08.2020
	 */
	public static boolean isNullOrEmpty(Object objeto) {
		return objeto == null || "".equals(objeto) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * Método que verifica si un objeto de tipo {@code List<?>} es nulo o vacío
	 *
	 * @param lista
	 *
	 * @return {@code TRUE} si es nulo o vacio sino {@code FALSE}
	 *
	 * @author jose.acosta 12.08.2020
	 */
	public static boolean isListNullOrEmpty(List<?> lista) {

		return (isNullOrEmpty(lista) || lista.size() == 0) ? Boolean.TRUE : Boolean.FALSE;

	}

	/**
	 * Método que obtiene un JSON a partir de un objeto proveído
	 * 
	 * @param objeto
	 * 
	 * @return {@code JSON}
	 * 
	 * @author jose.acosta 12.08.2020
	 */
	public static String getJsonFromObject(Object objeto) {

		return gson.toJson(objeto);

	}

	/**
	 * Método que obtiene un JSON a partir de un objeto proveído
	 * 
	 * @param objeto
	 * 
	 * @author jose.acosta 12.08.2020
	 * 
	 * @return {@code JSON}
	 */
	public static Object getObjectFromJson(String json, Class<?> objeto) {

		return gson.fromJson(json, objeto);

	}

	/**
	 * Función que retorna el número del mes de la fecha que se le pasa por
	 * parámetro
	 * 
	 * @param date
	 * @author jose.acosta 12.08.2020
	 * @return número del mes del 01 al 12
	 */
	public int obtenerMes(Date date) {
		if (null == date) {
			return 0;
		} else {
			String formato = "MM";
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(formato);
			return Integer.parseInt(dateFormat2.format(date));
		}
	}

	/**
	 * Metodo que recibe el patron a aplicar al numero
	 * 
	 * @param String recibe el patron para formatear un numero, los patrones son los
	 *               que se utilizan en la clase DecimalFormat
	 * @author jose.acosta 12.08.2020
	 */
	public void setNumberPattern(String sPattern) {
		dfn.applyPattern(sPattern);
	}

	/**
	 * Metodo que recibe un String de numero y devuelve un String formateado del
	 * numero
	 * 
	 * @author jose.acosta 12.08.2020
	 * @param String recibe la fecha a formateada Ej. '24/05/2006'
	 * @retun Date retorna el objeto de tipo Date
	 */
	public Date getDateParse(String sDate) throws Exception {
		Date dDateFormat = null;
		dDateFormat = dateFormat.parse(sDate);
		return dDateFormat;
	}

	/**
	 * Metodo que recibe el patron a aplicar a la fecha
	 * 
	 * @param String recibe el patron para formatear fechas, los patrones son los
	 *               que se utilizan en la clase SimpleDateFormat
	 * @author jose.acosta 12.08.2020
	 */
	public void setDatePattern(String sPattern) {
		dateFormat.applyPattern(sPattern);
	}

	/**
	 * Metodo que recibe un Objeto de tipo Date y devuelve un String formateado
	 * segun el string cargado en setDatePattern
	 * 
	 * @author jose.acosta 12.08.2020
	 * @param Date recibe un objeto de tipo Date
	 * @retun String retorna la fecha formateada Ej. '24/05/2006'
	 */
	public String getDateFormat(Date dDate) throws Exception {
		String sFormat = null;
		sFormat = dateFormat.format(dDate);
		return sFormat;
	}

	/**
	 * Metodo que recibe un Calendar y devuelve un String formateado segun el string
	 * cargado en setDatePattern
	 * 
	 * @author jose.acosta 12.08.2020
	 * @param Calendar recibe un objeto de tipo Calendar
	 * @retun String retorna la fecha formateada Ej. '24/05/2006'
	 */
	public String getDateFormat(Calendar dDate) throws Exception {
		String sFormat = null;
		sFormat = dateFormat.format(dDate);
		return sFormat;
	}

	/**
	 * Método que retorna un valor Booleando, {TRUE} si el parámetro pasado es
	 * numéico, {FALSE} si no es
	 * 
	 * @author jose.acosta 12.08.2020
	 * @param str
	 * @return
	 */

	public static boolean isNumeric(String str) {
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	/**
	 * METODO QUE DEBUELVE UN FORMATO DE FECHA ESTABLECIDO COMO PARAMETRO
	 * 
	 * @author carlos.barrera 22.04.2022
	 * @param formato FORMATO ESTABLECIDO A SER APLICADO
	 * @param FECHA A SER FORMATEADA
	 * */
	
	public static String formatoFecha(String formato, Date fecha) {
		
		if (formatosValidosFechas(formato)) {
			SimpleDateFormat fechaFormato = new SimpleDateFormat();
			fechaFormato.applyPattern(formato);
			return fechaFormato.format(fecha);
		} else {
			return null;
		}
		
		
	}
	
	
	/**
	 * METODO QUE VALIDA QUE EL FORMATO ESPECIFICADO SEA VALIDO
	 * 
	 * @author carlos.barrera 22.04.2022
	 * @param formato FORMATO ESTABLECIDO A SER APLICADO
	 * 
	 * */
	
	public static boolean formatosValidosFechas(String formato) {

		if (Constants.FORMATO_FECHA_DDMMYYYY_1.equals(formato) || Constants.FORMATO_FECHA_DDMMYYYY_2.equals(formato)
				|| Constants.FORMATO_FECHA_YYYYMMDD_1.equals(formato)
				|| Constants.FORMATO_FECHA_YYYYMMDD_2.equals(formato)
				|| Constants.FORMATO_FECHA_DDMMYYYY_1_HH_MM.equals(formato)
				|| Constants.FORMATO_FECHA_YYYYMMDD_3.equals(formato)
				|| Constants.FORMATO_FECHA_DDMMYY.equals(formato)
				|| Constants.FORMATO_FECHA_DDMMYY_2.equals(formato)) {

			return true;

		} else {

			return false;

		}

	}
	
	
	
}
