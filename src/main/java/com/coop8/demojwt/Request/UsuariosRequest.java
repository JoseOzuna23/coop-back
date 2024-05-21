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
public class UsuariosRequest implements Serializable {

	private static final long serialVersionUID = -681352131269084792L;

	private String username;
	private String searchText;

	private PaginacionRequest paginacion;
}