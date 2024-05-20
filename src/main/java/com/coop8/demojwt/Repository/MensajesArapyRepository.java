package com.coop8.demojwt.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coop8.demojwt.Models.MensajesArapy;


@Repository
public interface MensajesArapyRepository extends JpaRepository<MensajesArapy, Long> {

	/**
	 * Busca y obtiene un {Optional<MensajesArapy>} en base al campo mensaje
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @param mensaje
	 * @return Optional<MensajesArapy>
	 */
	Optional<MensajesArapy> findById(Long id);

	/**
	 * Busca y obtiene un {Page<MensajesArapy>} de todos los mensajesArapy
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @return Page<MensajesArapy>
	 */
	Page<MensajesArapy> findAllByOrderByMensajeAsc(Pageable pageable);

	/**
	 * Busca y obtiene un {Page<MensajesArapy>} en base al campo mensaje o autor
	 * 
	 * @author jose.acosta
	 * @since 20.04.2022
	 * @param mensaje, Autor
	 * @return Page<MensajesArapy>
	 */
	Page<MensajesArapy> findAllByMensajeContainingIgnoreCaseOrAutorContainingIgnoreCaseOrderByMensajeAsc(String mensaje, String autor,
			Pageable pageable);

	/**
	 * Elimina un registro en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 11.04.2022
	 * @param id
	 */
	void deleteById(Long id);
	
	
	@Query(value="SELECT * FROM mensajes_arapy ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
	Optional<MensajesArapy> getOneMessageRandom();

}