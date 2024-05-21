package com.coop8.demojwt.PayloadModels;

import java.io.Serializable;
import java.util.Date;

import com.coop8.demojwt.Models.Usuarios;
import com.coop8.demojwt.Utils.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosResponseModel implements Serializable {

    private static final long serialVersionUID = 8946817979422661252L;

    private Integer id;
    private String username;
    private String password;
    private String apellido;
    private String nombre;
    private Integer nroDoc;
    private String ipUltimoAcceso;
    private String fechaRegistro;
    private String estado;

    public void filterPayloadToSend(Usuarios usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.password = usuario.getPassword();
        this.apellido = usuario.getApellido();
        this.nombre = usuario.getNombre();
        this.nroDoc = usuario.getNroDoc();
        this.ipUltimoAcceso = usuario.getIpUltimoAcceso();
        this.fechaRegistro = DateUtil.formatoFecha("dd/MM/yyyy", usuario.getFechaRegistro());
        this.estado = usuario.getEstado().getDescripcion();
    }
}
