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
public class PaginacionResponse implements Serializable {

	private static final long serialVersionUID = 4639097437392590205L;

	private long totalItems;
	private long totalPages;
	private int currentPages;
	
}
