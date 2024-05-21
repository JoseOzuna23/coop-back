package com.coop8.demojwt.Request;

import java.io.Serializable;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest implements Serializable {

	private static final long serialVersionUID = -562067409640377017L;

	@NotBlank
	@NotNull
	private String password;

	@NotBlank
	@NotNull
	private String newPassword;

	@NotBlank
	@NotNull
	private String repeatNewPassword;
}
