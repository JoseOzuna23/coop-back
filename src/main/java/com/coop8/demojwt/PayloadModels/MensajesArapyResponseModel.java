package com.coop8.demojwt.PayloadModels;

import java.io.Serializable;

import com.coop8.demojwt.Models.MensajesArapy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensajesArapyResponseModel implements Serializable {

	private static final long serialVersionUID = 3481114364879410493L;

	private Long id;
	private String mensaje;
	private String autor;

	public void filterPayloadToSend(MensajesArapy mensajesArapy) {
		this.id = mensajesArapy.getId();
		this.mensaje = mensajesArapy.getMensaje();
		this.autor = mensajesArapy.getAutor();
	}
}