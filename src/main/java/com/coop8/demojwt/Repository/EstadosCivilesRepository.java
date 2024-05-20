package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.EstadosCiviles;


@Repository
public interface EstadosCivilesRepository extends JpaRepository<EstadosCiviles, Long> {

	/**
	 * Busca y obtiene un {Optional<EstadosCiviles>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param descripcion
	 * @return Optional<EstadosCiviles>
	 */
	Optional<EstadosCiviles> findById(Long id);

	/**
	 * Busca y obtiene un {Page<EstadosCiviles>} de todos los estados
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @return Page<EstadosCiviles>
	 */
	Page<EstadosCiviles> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<EstadosCiviles>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param descripcion
	 * @return Page<EstadosCiviles>
	 */
	Page<EstadosCiviles> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
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