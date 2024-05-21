package com.coop8.demojwt.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.coop8.demojwt.Jwt.JwtService;
import com.coop8.demojwt.Models.EstadosPersonas;
import com.coop8.demojwt.PayloadModels.EstadosPersonasResponseModel;
import com.coop8.demojwt.Repository.EstadosPersonasRepository;
import com.coop8.demojwt.Request.EstadosPersonasRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.EstadosPersonasResponse;
import com.coop8.demojwt.Response.PaginacionResponse;
import com.coop8.demojwt.Response.Response;
import com.coop8.demojwt.Response.ResponseHeader;
import com.coop8.demojwt.Response.SecuredResponse;
import com.coop8.demojwt.Utils.ECodigosRespuestas;
import com.coop8.demojwt.Utils.Util;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class EstadosPersonasService {

	@Autowired
	EstadosPersonasRepository estadoPersonaRepository;

	@Autowired
	JwtService jwtService;

	/**
	 * Busca y obtiene un {Optional<EstadosPersonas>} en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param requestData
	 * @return Optional<EstadosPersonas>
	 */
	public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

		log.info("EstadosPersonasService	| getById");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));
		// Verificamos la Firma Digital del dato recibido
		//arapyApiSecurityService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		EstadosPersonasRequest estadoPersonasRequest = (EstadosPersonasRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), EstadosPersonasRequest.class);
		log.info("__estadoPersonasRequest:	" + Util.getJsonFromObject(estadoPersonasRequest));

		EstadosPersonasResponse estadoPersonaResponse = new EstadosPersonasResponse();

		long idEstado = estadoPersonasRequest.getId();
		Optional<EstadosPersonas> estadoPersona = estadoPersonaRepository.findById(idEstado);

		if (estadoPersona.isPresent()) {
			List<EstadosPersonas> estadoPersonaList = new ArrayList<EstadosPersonas>();
			estadoPersonaList.add(estadoPersona.get());

			List<EstadosPersonasResponseModel> estadoPersonaResponseModels = new ArrayList<>();

			for (EstadosPersonas estadoPersonas : estadoPersonaList) {
				EstadosPersonasResponseModel eu = new EstadosPersonasResponseModel();
				eu.filterPayloadToSend(estadoPersonas);
				estadoPersonaResponseModels.add(eu);
			}

			estadoPersonaResponse.setEstadosPersonas(estadoPersonaResponseModels);
		}

		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
		header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
		response.setHeader(header);
		response.setData(estadoPersonaResponse);

		log.info("__EstadosPersonasResponse:	" + Util.getJsonFromObject(estadoPersonaResponse));
		securedResponse.setData(jwtService.getDataFromPayload(response));
		log.info("__response:	" + Util.getJsonFromObject(response));
		log.info("__securedResponse:	" + Util.getJsonFromObject(securedResponse));

		return securedResponse;
	}

	/**
	 * Método que retorna lista pageable de estadoPersona
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param requestData
	 * @return {SecuredResponse}
	 * @throws Exception
	 */
	public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

		log.info("EstadosPersonasService	| list");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));

		// Verificamos la Firma Digital del dato recibido
	//	arapyApiSecurityService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		EstadosPersonasRequest estadoPersonasRequest = (EstadosPersonasRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), EstadosPersonasRequest.class);
		log.info("__estadoPersonasRequest:	" + Util.getJsonFromObject(estadoPersonasRequest));

		List<EstadosPersonas> estadoPersonaList = new ArrayList<>();

		int pagina = estadoPersonasRequest.getPaginacion().getPagina() - 1;

		Pageable paging = PageRequest.of(pagina, estadoPersonasRequest.getPaginacion().getCantidad());

		Page<EstadosPersonas> pageEstadosPersonas;

		if (Util.isNullOrEmpty(estadoPersonasRequest.getDescripcion()))
			pageEstadosPersonas = estadoPersonaRepository.findAllByOrderByDescripcionAsc(paging);
		else
			pageEstadosPersonas = estadoPersonaRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
					estadoPersonasRequest.getDescripcion(), paging);

		estadoPersonaList = pageEstadosPersonas.getContent();

		List<EstadosPersonasResponseModel> estadoPersonaResponseModels = new ArrayList<>();

		for (EstadosPersonas estadoPersona : estadoPersonaList) {
			EstadosPersonasResponseModel eu = new EstadosPersonasResponseModel();
			eu.filterPayloadToSend(estadoPersona);
			estadoPersonaResponseModels.add(eu);
		}

		EstadosPersonasResponse estadoPersonaResponse = new EstadosPersonasResponse();
		estadoPersonaResponse.setEstadosPersonas(estadoPersonaResponseModels);
		PaginacionResponse pageableResponse = new PaginacionResponse();

		pageableResponse.setTotalItems(pageEstadosPersonas.getTotalElements());
		pageableResponse.setTotalPages(pageEstadosPersonas.getTotalPages());
		pageableResponse.setCurrentPages(pageEstadosPersonas.getNumber() + 1);

		estadoPersonaResponse.setPaginacion(pageableResponse);

		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
		header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
		response.setHeader(header);
		response.setData(estadoPersonaResponse);

		log.info("__EstadosPersonasResponse:	" + Util.getJsonFromObject(estadoPersonaResponse));
		securedResponse.setData(jwtService.getDataFromPayload(response));
		log.info("__response:	" + Util.getJsonFromObject(response));
		log.info("__securedResponse:	" + Util.getJsonFromObject(securedResponse));
		return securedResponse;
	}

	/**
	 * Método que elimina un registro de la tabla estadoPersona según el ID del
	 * estadoPersona pasado por parámetro
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * @param requestData
	 * @return {SecuredResponse}
	 * @throws Exception
	 */
	@Transactional
	public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

		log.info("EstadosPersonasService	| deleteById");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));
		// Verificamos la Firma Digital del dato recibido
	//	arapyApiSecurityService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		EstadosPersonasRequest estadoPersonasRequest = (EstadosPersonasRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), EstadosPersonasRequest.class);
		log.info("__estadoPersonasRequest:	" + Util.getJsonFromObject(estadoPersonasRequest));

		long idEstado = estadoPersonasRequest.getId();
		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		estadoPersonaRepository.deleteById(idEstado);
		header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
		header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
		response.setHeader(header);
		response.setData(null);

		securedResponse.setData(jwtService.getDataFromPayload(response));
		log.info("__response:	" + Util.getJsonFromObject(response));
		log.info("__securedResponse:	" + Util.getJsonFromObject(securedResponse));

		return securedResponse;
	}

	/**
	 * Método que Inserta un registro nuevo en la tabla estadoPersona, o
	 * actualiza un estadoPersona existente en ella. Esto según si el objeto recibido por
	 * parámetro tiene o no un ID
	 * 
	 * @author carlos.barrera
	 * @since 07.04.2022
	 * 
	 * @param requestData
	 * @return {SecuredResponse}
	 * @throws Exception
	 */
	public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("EstadosPersonasService	| save");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));

		// Verificamos la Firma Digital del dato recibido
	//	arapyApiSecurityService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		EstadosPersonasRequest estadoPersonasRequest = (EstadosPersonasRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), EstadosPersonasRequest.class);
		log.info("__estadoPersonasRequest:	" + Util.getJsonFromObject(estadoPersonasRequest));

		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		// obtenemos el usuario de SecurityContextHolder
		String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

		// verificamos si es un INSERT o un UPDATE
		if (Util.isNullOrEmpty(estadoPersonasRequest.getId()) || estadoPersonasRequest.getId() == 0 ) {
			// INSERT
			EstadosPersonas estadoPersonaInsert = new EstadosPersonas();
			estadoPersonaInsert.setDescripcion(estadoPersonasRequest.getDescripcion());
			estadoPersonaInsert.setUsuariosys(usuariosys);

			estadoPersonaRepository.save(estadoPersonaInsert);
		} else {
			// UPDATE
			long idEstado = estadoPersonasRequest.getId();

			EstadosPersonas estadoPersonaUpdate = estadoPersonaRepository.findById(idEstado).get();
			estadoPersonaUpdate.setDescripcion(estadoPersonasRequest.getDescripcion());
			estadoPersonaUpdate.setUsuariosys(usuariosys);

			estadoPersonaRepository.save(estadoPersonaUpdate);
		}

		header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
		header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
		response.setHeader(header);
		response.setData(null);

		securedResponse.setData(jwtService.getDataFromPayload(response));
		log.info("__response:	" + Util.getJsonFromObject(response));
		log.info("__securedResponse:	" + Util.getJsonFromObject(securedResponse));
		return securedResponse;
	}

	/*
	 * public SecuredResponse nuevo(@Valid @RequestBody RequestData requestData)
	 * throws Exception { log.info("EstadosPersonasService	| nuevo");
	 * log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));
	 * 
	 * // Verificamos la Firma Digital del dato recibido
	 * arapyApiSecurityService.validateDataSignature(requestData);
	 * 
	 * // decodificamos el dataRequest y obtenemos el Payload estadoPersonasRequest
	 * estadoPersonasRequest = (EstadosPersonasRequest) arapyApiSecurityService
	 * .getPayloadClassFromToken(requestData.getData(),
	 * estadoPersonasRequest.class); log.info("__EstadosPersonasRequest:	" +
	 * Util.getJsonFromObject(EstadosPersonasRequest));
	 * 
	 * // obtenemos el usuario de SecurityContextHolder String usuariosys =
	 * SecurityContextHolder.getContext().getAuthentication().getName();
	 * 
	 * return securedResponse; }
	 */
}
