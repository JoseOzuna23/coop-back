package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.TiposDocumentos;


@Repository
public interface TiposDocumentosRepository extends JpaRepository<TiposDocumentos, Long> {

	/**
	 * Busca y obtiene un {Optional<TiposDocumentos>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @param descripcion
	 * @return Optional<TiposDocumentos>
	 */
	Optional<TiposDocumentos> findById(Long id);

	/**
	 * Busca y obtiene un {Page<TiposDocumentos>} de todos los tiposDocumentos
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @return Page<TiposDocumentos>
	 */
	Page<TiposDocumentos> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<TiposDocumentos>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @param descripcion
	 * @return Page<TiposDocumentos>
	 */
	Page<TiposDocumentos> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @param id
	 */
	void deleteById(Long id);

}