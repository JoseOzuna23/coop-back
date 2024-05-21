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
public class Response implements Serializable {

	private static final long serialVersionUID = -5580657660242082917L;

	@NotEmpty
	@NonNull
	private ResponseHeader header;
	private Object data;
}
