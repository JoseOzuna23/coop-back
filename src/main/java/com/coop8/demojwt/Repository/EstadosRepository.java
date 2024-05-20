package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.Estados;


@Repository
public interface EstadosRepository extends JpaRepository<Estados, Long> {

	/**
	 * Busca y obtiene un {Optional<Estados>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 04.04.2022
	 * @param descripcion
	 * @return Optional<Estados>
	 */
	Optional<Estados> findById(Long id);

	/**
	 * Busca y obtiene un {Page<Estados>} de todos los estados
	 * 
	 * @author carlos.barrera
	 * @since 04.04.2022
	 * @return Page<Estados>
	 */
	Page<Estados> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<Estados>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 04.04.2022
	 * @param descripcion
	 * @return Page<Estados>
	 */
	Page<Estados> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 04.04.2022
	 * @param id
	 */
	void deleteById(Long id);

}