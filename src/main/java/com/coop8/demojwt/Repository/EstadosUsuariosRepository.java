package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.EstadosUsuarios;


@Repository
public interface EstadosUsuariosRepository extends JpaRepository<EstadosUsuarios, Long> {

	/**
	 * Busca y obtiene un {Optional<EstadosUsuarios>} en base al campo descripcion
	 * 
	 * @author brenda.maiz
	 * @since 09.03.2022
	 * @param descripcion
	 * @return Optional<EstadosUsuarios>
	 */
	Optional<EstadosUsuarios> findById(Long id);

	/**
	 * Busca y obtiene un {Page<EstadosUsuarios>} de todos los estados de usuarios
	 * 
	 * @author brenda.maiz
	 * @since 09.03.2022
	 * @return Page<EstadosUsuarios>
	 */
	Page<EstadosUsuarios> findAllByOrderByDescripcionAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<EstadosUsuarios>} en base al campo descripcion
	 * 
	 * @author brenda.maiz
	 * @since 09.03.2022
	 * @param descripcion
	 * @return Page<EstadosUsuarios>
	 */
	Page<EstadosUsuarios> findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String descripcion,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author brenda.maiz
	 * @since 09.03.2022
	 * @param id
	 */
	void deleteById(Long id);

}