package com.coop8.demojwt.Response;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse implements Serializable {

	private static final long serialVersionUID = 8350177299175495750L;

	@NotBlank
	@NotNull
	private String usuario;

	@NotBlank
	@NotNull
	private String nroDoc;

	@NotBlank
	@NotNull
	private String nombresApellidos;

	@NotBlank
	@NotNull
	private String tokenType;

	@NotBlank
	@NotNull
	private String accessToken;

	@NotBlank
	@NotNull
	private String refreshToken;

	@NotBlank
	@NotNull
	private Timestamp fechaCaducaPassword;

	private Timestamp fechaUltimoAcceso;

}
