package com.coop8.demojwt.PayloadModels;

import java.io.Serializable;

import com.coop8.demojwt.Models.TiposDocumentos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TiposDocumentosResponseModel implements Serializable {

	private static final long serialVersionUID = 3481114364879410493L;

	private Long id;
	private String descripcion;

	public void filterPayloadToSend(TiposDocumentos tiposDocumentos) {
		this.id = tiposDocumentos.getId();
		this.descripcion = tiposDocumentos.getDescripcion();
	}
}