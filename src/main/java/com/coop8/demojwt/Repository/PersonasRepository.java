package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.Personas;


@Repository
public interface PersonasRepository extends JpaRepository<Personas, Long> {

	/**
	 * Busca y obtiene un {Optional<Personas>} en base al campo persona
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @param persona
	 * @return Optional<Personas>
	 */
	Optional<Personas> findById(Long id);

	/**
	 * Busca y obtiene un {Page<Personas>} de todos los personas
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @return Page<Personas>
	 */
	Page<Personas> findAllByOrderByNroDocumentoAsc(Pageable pageable);
	
	

	/**
	 * Busca y obtiene un {Page<Personas>} en base al campo persona
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @param persona
	 * @return Page<Personas>
	 */
	Page<Personas> findByNroDocumentoContainingIgnoreCaseOrderByNroDocumentoAsc(String nroDocumento,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @param id
	 */
	void deleteById(Long id);

}