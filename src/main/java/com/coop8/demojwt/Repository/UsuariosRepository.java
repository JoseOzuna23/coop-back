package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.Usuarios;



public interface UsuariosRepository extends JpaRepository<Usuarios,Integer> {
    Optional<Usuarios> findByUsername(String username); 
}
