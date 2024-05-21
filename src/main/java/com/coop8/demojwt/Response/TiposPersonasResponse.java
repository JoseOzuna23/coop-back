package com.coop8.demojwt.Response;

import java.io.Serializable;
import java.util.List;

import com.coop8.demojwt.PayloadModels.TiposPersonasResponseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TiposPersonasResponse implements Serializable {

	private static final long serialVersionUID = 8843219508588806231L;

	private List<TiposPersonasResponseModel> tiposPersonas;
	private PaginacionResponse paginacion;

}
