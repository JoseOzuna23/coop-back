package com.coop8.demojwt.Response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse implements Serializable {

	private static final long serialVersionUID = 8586468516087422002L;

	private String tokenType;
	private String accessToken;

}
