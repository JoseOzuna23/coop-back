package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.EstadosPersonas;


@Repository
public interface EstadosPersonasRepository extends JpaRepository<EstadosPersonas, Long> {

	/**
	 * Busca y obtiene un {Optional<EstadosPersonas>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param descripcion
	 * @return Optional<EstadosPersonas>
	 */
	Optional<EstadosPersonas> findById(Long id);

	/**
	 * Busca y obtiene un {Page<EstadosPersonas>} de todos los estados
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @return Page<EstadosPersonas>
	 */
	Page<EstadosPersonas> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<EstadosPersonas>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param descripcion
	 * @return Page<EstadosPersonas>
	 */
	Page<EstadosPersonas> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param id
	 */
	void deleteById(Long id);

}