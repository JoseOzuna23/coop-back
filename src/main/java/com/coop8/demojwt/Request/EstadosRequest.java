package com.coop8.demojwt.Request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadosRequest implements Serializable {

	private static final long serialVersionUID = -5858040595024021418L;

	private int id;
	private String descripcion;
	
	private PaginacionRequest paginacion;

}
