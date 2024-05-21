package com.coop8.demojwt.PayloadModels;

import java.io.Serializable;

import com.coop8.demojwt.Models.Personas;
import com.coop8.demojwt.Utils.DateUtil; // Importa la nueva clase utilitaria

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonasResponseModel implements Serializable {

    private static final long serialVersionUID = 3481114364879410493L;

    private Long id;
    private String nroDocumento;
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private String sexo;
    private String tipoDocumento;
    private String tipoPersona;
    private String estadoCivil;
    private String localidadesNac;
    private String localidadesRes;
    private String nacionalidad;

    public void filterPayloadToSend(Personas personas) {
        this.id = personas.getId();
        this.nroDocumento = personas.getNroDocumento();
        this.nombres = personas.getNombre1().concat("  ".concat(personas.getNombre2()));
        this.apellidos = personas.getApellido1().concat("  ".concat(personas.getApellido2()));
        this.fechaNacimiento = DateUtil.formatoFecha("dd/MM/yyyy", personas.getFechaNacimiento());
        this.sexo = personas.getSexo();
        this.tipoDocumento = personas.getTipoDocumento().getDescripcion();
        this.tipoPersona = personas.getTipoPersona().getDescripcion();
        this.estadoCivil = personas.getEstadoCivil().getDescripcion();
        this.localidadesNac = personas.getLocalidadesNac().getDescripcion();
        this.localidadesRes = personas.getLocalidadesRes().getDescripcion();
        this.nacionalidad = personas.getNacionalidad().getDescripcion();
    }
}
