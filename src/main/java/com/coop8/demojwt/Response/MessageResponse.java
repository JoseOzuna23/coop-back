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
public class MessageResponse implements Serializable {

	private static final long serialVersionUID = -5247042299880741609L;
	private String message;
}
