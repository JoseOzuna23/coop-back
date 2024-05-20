package com.coop8.demojwt.Models;

import java.io.Serializable;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Roles implements Serializable {

	private static final long serialVersionUID = -4261257792896823348L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "descripcion", unique = true, length = 100)
	private String descripcion;

	@NotNull
	@Column(name = "observacion", length = 500)
	private String observacion;

	@Column(name = "usuariosys", length = 50)
	@NotNull
	private String usuariosys;
}