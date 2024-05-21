package com.coop8.demojwt.Request;

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
public class RequestData implements Serializable {

	private static final long serialVersionUID = -4240483531378025992L;

	@NotEmpty
	@NonNull
	private String data;

}
