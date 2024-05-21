package com.coop8.demojwt.Response;

import java.io.Serializable;
import java.util.List;

import com.coop8.demojwt.PayloadModels.UsuariosResponseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosResponse implements Serializable {

	private static final long serialVersionUID = -2468965944518631677L;

	private List<UsuariosResponseModel> usuarios;
	private PaginacionResponse paginacion;
}