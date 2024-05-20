package com.coop8.demojwt.Models;

import java.io.Serializable;
import java.util.Date;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "personas")
public class Personas implements Serializable {

	private static final long serialVersionUID = 8510122407262613133L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SERIAL")
	private Long id;

	@NotNull
	@Column(name = "nro_docu", length = 15, unique = true)
	private String nroDocumento;

	@NotNull
	@Column(name = "nombre_1", length = 30)
	private String nombre1;

	@Column(name = "nombre_2", length = 30)
	private String nombre2;

	@Column(name = "nombre_3", length = 30)
	private String nombre3;

	@NotNull
	@Column(name = "apellido_1", length = 30)
	private String apellido1;

	@Column(name = "apellido_2", length = 30)
	private String apellido2;

	@Column(name = "apellido_3", length = 30)
	private String apellido3;

	@NotNull
	@Column(name = "fecha_nac", length = 10)
	private Date fechaNacimiento;

	@NotNull
	@Column(name = "sexo", length = 1)
	private String sexo;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_docu", referencedColumnName = "id")
	private TiposDocumentos tipoDocumento;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_persona", referencedColumnName = "id")
	private TiposPersonas tipoPersona;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_estado_civil", referencedColumnName = "id")
	private EstadosCiviles estadoCivil;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_localidad_nac", referencedColumnName = "id")
	private Localidades localidadesNac;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_localidad_res", referencedColumnName = "id")
	private Localidades localidadesRes;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_nacionalidad", referencedColumnName = "id")
	private Nacionalidades nacionalidad;

	@Column(name = "fechasys", length = 20)
	@NotNull
	private Date fechasys;

	@Column(name = "usuariosys", length = 50)
	@NotNull
	private String usuariosys;
}
