package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.Nacionalidades;


@Repository
public interface NacionalidadesRepository extends JpaRepository<Nacionalidades, Long> {

	/**
	 * Busca y obtiene un {Optional<Nacionalidades>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 08.04.2022
	 * @param descripcion
	 * @return Optional<Nacionalidades>
	 */
	Optional<Nacionalidades> findById(Long id);

	/**
	 * Busca y obtiene un {Page<Nacionalidades>} de todos los nacionalidades
	 * 
	 * @author carlos.barrera
	 * @since 08.04.2022
	 * @return Page<Nacionalidades>
	 */
	Page<Nacionalidades> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<Nacionalidades>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 08.04.2022
	 * @param descripcion
	 * @return Page<Nacionalidades>
	 */
	Page<Nacionalidades> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 08.04.2022
	 * @param id
	 */
	void deleteById(Long id);

}