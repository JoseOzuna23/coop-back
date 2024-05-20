package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.TiposPersonas;


@Repository
public interface TiposPersonasRepository extends JpaRepository<TiposPersonas, Long> {

	/**
	 * Busca y obtiene un {Optional<TiposPersonas>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param descripcion
	 * @return Optional<TiposPersonas>
	 */
	Optional<TiposPersonas> findById(Long id);

	/**
	 * Busca y obtiene un {Page<TiposPersonas>} de todos los tiposPersonas
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @return Page<TiposPersonas>
	 */
	Page<TiposPersonas> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<TiposPersonas>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param descripcion
	 * @return Page<TiposPersonas>
	 */
	Page<TiposPersonas> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
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