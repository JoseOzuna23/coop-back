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
public class PaginacionRequest implements Serializable{

	private static final long serialVersionUID = -3837832127546539021L;
	
	private int pagina;
	private int cantidad;
	//private String [] sort;
	
}
