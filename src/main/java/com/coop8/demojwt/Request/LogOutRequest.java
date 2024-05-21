package com.coop8.demojwt.Request;

import java.io.Serializable;

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
public class LogOutRequest implements Serializable {

	private static final long serialVersionUID = 2447161253397436576L;

	@NotBlank
	@NotNull
	private String sesion;

}
