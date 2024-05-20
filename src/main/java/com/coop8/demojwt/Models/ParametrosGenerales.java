package com.coop8.demojwt.Models;

import java.io.Serializable;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parametros_generales")
public class ParametrosGenerales implements Serializable {

	private static final long serialVersionUID = -1499880738090726990L;

	@Id
	@Column(length = 100)
	private String id;

	@Column(name = "valor", unique = true, length = 100)
	@NonNull
	private String valor;

	@NotNull
	@Column(name = "observacion", length = 500)
	private String observacion;

	@Column(name = "usuariosys", length = 50)
	@NotNull
	private String usuariosys;
}