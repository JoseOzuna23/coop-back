package com.coop8.demojwt.Models;

import java.io.Serializable;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mensajes_arapy")
public class MensajesArapy implements Serializable {

	private static final long serialVersionUID = 8510122407262613133L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SERIAL")
	private Long id;

	@NotNull
	@Column(name = "mensaje", unique = true, length = 500)
	private String mensaje;
	
	@NotNull
	@Column(name = "autor", length = 100)
	private String autor;

	@Column(name = "usuariosys", length = 50)
	@NotNull
	private String usuariosys;
}
