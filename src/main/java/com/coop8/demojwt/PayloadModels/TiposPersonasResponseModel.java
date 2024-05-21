package com.coop8.demojwt.PayloadModels;

import java.io.Serializable;

import com.coop8.demojwt.Models.TiposPersonas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TiposPersonasResponseModel implements Serializable {

	private static final long serialVersionUID = 3481114364879410493L;

	private Long id;
	private String descripcion;

	public void filterPayloadToSend(TiposPersonas tiposPersonas) {
		this.id = tiposPersonas.getId();
		this.descripcion = tiposPersonas.getDescripcion();
	}
}