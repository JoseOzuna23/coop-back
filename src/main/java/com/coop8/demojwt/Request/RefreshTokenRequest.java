package com.coop8.demojwt.Request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest implements Serializable {

	private static final long serialVersionUID = 7088150697917383827L;
		
	@NotBlank
	@NotNull
	private String refreshToken;
}
