package com.coop8.demojwt.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ECodigosRespuestas {

	/**
	 * Clase de tipo Enum, que almacena los códigos y textos de mensajes de
	 * respuestas para el header del response
	 * 
	 * Solo se debe de utilizar 2 (dos) códigos de respuestas 200 - Correcto 400 -
	 * Fallido
	 * 
	 * @author Picmna
	 * @since 17.05.2024
	 * @mail tadeo.ramirez.950208@gmail.com
	 */
	SUCCESS(200, "SUCCESS"), 
	SUCCESS_LOGOUT(200, "Su sesión ha sido cerrada"),
	ERROR(400, "Ha ocurrido un error inesperado"), 
	ERROR_LOGIN_FAILED(400, "Usuario y/o Contraseña Incorrectos"),
	ERROR_CRITICO_DE_SEGURIDAD(400, "Usted no es bienvenido aquí"),
	ERROR_NO_AUTORIZADO(400, "Usted debe autenticarse");

	private int codigoRespuesta;
	private String txtRespuesta;

}
