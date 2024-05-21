package com.coop8.demojwt.Response;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHeader implements Serializable {

	private static final long serialVersionUID = 6555800440481630072L;

	@NotEmpty
	private int codResultado;

	@NotEmpty
	@NonNull
	private String txtResultado;

}
