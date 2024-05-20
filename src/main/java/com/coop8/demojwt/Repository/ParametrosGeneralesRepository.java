package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.ParametrosGenerales;


@Repository
public interface ParametrosGeneralesRepository extends JpaRepository<ParametrosGenerales, String> {

	/**
	 * Busca y obtiene un {Optional<ParametrosGenerales>} en base al campo id
	 * 
	 * @author jose.acosta
	 * @since 04.03.2022
	 * @mail acrajovi@gmail.com
	 * @param id
	 * @return Optional<ParametrosGenerales>
	 */
	Optional<ParametrosGenerales> findById(String id);
}
