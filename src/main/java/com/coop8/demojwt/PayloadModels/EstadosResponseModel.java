package com.coop8.demojwt.PayloadModels;

import java.io.Serializable;

import com.coop8.demojwt.Models.Estados;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadosResponseModel implements Serializable {

	private static final long serialVersionUID = 3481114364879410493L;

	private Long id;
	private String descripcion;

	public void filterPayloadToSend(Estados estados) {
		this.id = estados.getId();
		this.descripcion = estados.getDescripcion();
	}
}