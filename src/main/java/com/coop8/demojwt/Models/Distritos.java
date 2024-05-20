package com.coop8.demojwt.Models;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "distritos")
public class Distritos implements Serializable {
	
private static final long serialVersionUID = 5315661250214684441L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SERIAL")
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id")
	private Departamentos departamento;	
	@NotNull
	
	
	@Column(name = "descripcion", length = 100)
	private String descripcion;	
	
	
	@Column(name = "cod_distrito_sicoop")
	private Number cod_distrito_sicoop;
	
	@Column(name = "cod_distrito_set")
	private Number cod_distrito_set;
	
	
	@Column(name = "usuariosys", length = 50)
	@NotNull
	private String usuariosys;
	
	@Column(name = "fechasys", length = 20)
	@NotNull
	private Date fechasys;
}
