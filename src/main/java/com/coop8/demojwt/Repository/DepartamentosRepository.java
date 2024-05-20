package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.Departamentos;


@Repository
public interface DepartamentosRepository extends JpaRepository<Departamentos, Long> {

	/**
	 * Busca y obtiene un {Optional<Departamentos>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 19.04.2022
	 * @param descripcion
	 * @return Optional<Departamentos>
	 */
	Optional<Departamentos> findById(Long id);

	/**
	 * Busca y obtiene un {Page<Departamentos>} de todos los departamentos
	 * 
	 * @author carlos.barrera
	 * @since 19.04.2022
	 * @return Page<Departamentos>
	 */
	Page<Departamentos> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<Departamentos>} en base al campo descripcion
	 * 
	 * @author carlos.barrera
	 * @since 19.04.2022
	 * @param descripcion
	 * @return Page<Departamentos>
	 */
	Page<Departamentos> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 19.04.2022
	 * @param id
	 */
	void deleteById(Long id);

}