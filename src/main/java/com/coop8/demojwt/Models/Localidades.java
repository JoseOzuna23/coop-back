package com.coop8.demojwt.Models;

import java.io.Serializable;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "localidades")
public class Localidades implements Serializable {

	private static final long serialVersionUID = 8510122407262613133L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SERIAL")
	private Long id;

	@NotNull
	@Column(name = "descripcion", length = 100)
	private String descripcion;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id")
	private Departamentos departamento;
	
	
	
	@Column(name = "usuariosys", length = 50)
	@NotNull
	private String usuariosys;
}
