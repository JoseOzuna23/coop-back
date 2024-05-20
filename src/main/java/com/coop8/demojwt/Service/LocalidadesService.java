package com.coop8.demojwt.Service;

import com.coop8.demojwt.Jwt.JwtService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.coop8.demojwt.Models.Localidades;
import com.coop8.demojwt.PayloadModels.LocalidadesResponseModel;
import com.coop8.demojwt.Repository.LocalidadesRepository;
import com.coop8.demojwt.Request.LocalidadesRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.LocalidadesResponse;
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
public class LocalidadesService {

	@Autowired
	LocalidadesRepository localidadesRepository;

	@Autowired
	JwtService jwtService;

	/**
	 * Busca y obtiene un {Optional<Localidades>} en base al campo id
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @param requestData
	 * @return Optional<Localidades>
	 */
	public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

		log.info("LocalidadesService	| getById");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));
		// Verificamos la Firma Digital del dato recibido
	//	jwtService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		LocalidadesRequest localidadesRequest = (LocalidadesRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), LocalidadesRequest.class);
		log.info("__localidadesRequest:	" + Util.getJsonFromObject(localidadesRequest));

		LocalidadesResponse localidadesResponse = new LocalidadesResponse();

		long idLocalidad = localidadesRequest.getId();
		Optional<Localidades> localidad = localidadesRepository.findById(idLocalidad);

		if (localidad.isPresent()) {
			List<Localidades> localidadesList = new ArrayList<Localidades>();
			localidadesList.add(localidad.get());

			List<LocalidadesResponseModel> localidadesResponseModels = new ArrayList<>();

			for (Localidades localidades : localidadesList) {
				LocalidadesResponseModel eu = new LocalidadesResponseModel();
				eu.filterPayloadToSend(localidades);
				localidadesResponseModels.add(eu);
			}

			localidadesResponse.setLocalidades(localidadesResponseModels);
		}

		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
		header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
		response.setHeader(header);
		response.setData(localidadesResponse);

		log.info("__LocalidadesResponse:	" + Util.getJsonFromObject(localidadesResponse));
		securedResponse.setData(jwtService.getDataFromPayload(response));
		log.info("__response:	" + Util.getJsonFromObject(response));
		log.info("__securedResponse:	" + Util.getJsonFromObject(securedResponse));

		return securedResponse;
	}

	/**
	 * Método que retorna lista pageable de localidades
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @param requestData
	 * @return {SecuredResponse}
	 * @throws Exception
	 */
	public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

		log.info("LocalidadesService	| list");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));

		// Verificamos la Firma Digital del dato recibido
	//	jwtService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		LocalidadesRequest localidadesRequest = (LocalidadesRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), LocalidadesRequest.class);
		log.info("__localidadesRequest:	" + Util.getJsonFromObject(localidadesRequest));

		List<Localidades> localidadesList = new ArrayList<>();

		int pagina = localidadesRequest.getPaginacion().getPagina() - 1;

		Pageable paging = PageRequest.of(pagina, localidadesRequest.getPaginacion().getCantidad());

		Page<Localidades> pageLocalidades;

		if (Util.isNullOrEmpty(localidadesRequest.getDescripcion()))
			pageLocalidades = localidadesRepository.findAllByOrderByDescripcionAsc(paging);
		else
			pageLocalidades = localidadesRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
					localidadesRequest.getDescripcion(), paging);

		localidadesList = pageLocalidades.getContent();

		List<LocalidadesResponseModel> localidadesResponseModels = new ArrayList<>();

		for (Localidades localidades : localidadesList) {
			LocalidadesResponseModel eu = new LocalidadesResponseModel();
			eu.filterPayloadToSend(localidades);
			localidadesResponseModels.add(eu);
		}

		LocalidadesResponse localidadesResponse = new LocalidadesResponse();
		localidadesResponse.setLocalidades(localidadesResponseModels);
		PaginacionResponse pageableResponse = new PaginacionResponse();

		pageableResponse.setTotalItems(pageLocalidades.getTotalElements());
		pageableResponse.setTotalPages(pageLocalidades.getTotalPages());
		pageableResponse.setCurrentPages(pageLocalidades.getNumber() + 1);

		localidadesResponse.setPaginacion(pageableResponse);

		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
		header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
		response.setHeader(header);
		response.setData(localidadesResponse);

		log.info("__LocalidadesResponse:	" + Util.getJsonFromObject(localidadesResponse));
		securedResponse.setData(jwtService.getDataFromPayload(response));
		log.info("__response:	" + Util.getJsonFromObject(response));
		log.info("__securedResponse:	" + Util.getJsonFromObject(securedResponse));
		return securedResponse;
	}

	/**
	 * Método que elimina un registro de la tabla localidades según el ID del
	 * localidad pasado por parámetro
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * @param requestData
	 * @return {SecuredResponse}
	 * @throws Exception
	 */
	@Transactional
	public SecuredResponse deleteById(@Valid @RequestBody RequestData
        requestData) throws Exception {

		log.info("LocalidadesService	| deleteById");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));
		// Verificamos la Firma Digital del dato recibido
		//jwtService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		LocalidadesRequest localidadesRequest = (LocalidadesRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), LocalidadesRequest.class);
		log.info("__localidadesRequest:	" + Util.getJsonFromObject(localidadesRequest));

		long idLocalidad = localidadesRequest.getId();
		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		localidadesRepository.deleteById(idLocalidad);
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
	 * Método que Inserta un registro nuevo en la tabla localidades, o
	 * actualiza un localidad existente en ella. Esto según si el objeto recibido por
	 * parámetro tiene o no un ID
	 * 
	 * @author carlos.barrera
	 * @since 20.04.2022
	 * 
	 * @param requestData
	 * @return {SecuredResponse}
	 * @throws Exception
	 */
	public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("LocalidadesService	| save");
		log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));

		// Verificamos la Firma Digital del dato recibido
		//jwtService.validateDataSignature(requestData);

		// decodificamos el dataRequest y obtenemos el Payload
		LocalidadesRequest localidadesRequest = (LocalidadesRequest) jwtService
				.getPayloadClassFromToken(requestData.getData(), LocalidadesRequest.class);
		log.info("__localidadesRequest:	" + Util.getJsonFromObject(localidadesRequest));

		ResponseHeader header = new ResponseHeader();
		Response response = new Response();
		SecuredResponse securedResponse = new SecuredResponse();

		// obtenemos el usuario de SecurityContextHolder
		String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

		// verificamos si es un INSERT o un UPDATE
		if (Util.isNullOrEmpty(localidadesRequest.getId()) || localidadesRequest.getId() == 0) {
			// INSERT
			Localidades localidadInsert = new Localidades();
			localidadInsert.setDescripcion(localidadesRequest.getDescripcion().toUpperCase());
			localidadInsert.setUsuariosys(usuariosys);

			localidadesRepository.save(localidadInsert);
		} else {
			// UPDATE
			long idLocalidad = localidadesRequest.getId();

			Localidades localidadUpdate = localidadesRepository.findById(idLocalidad).get();
			localidadUpdate.setDescripcion(localidadesRequest.getDescripcion().toUpperCase());
			localidadUpdate.setUsuariosys(usuariosys);

			localidadesRepository.save(localidadUpdate);
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
	 * throws Exception { log.info("LocalidadesService	| nuevo");
	 * log.info("__dataRequest:	" + Util.getJsonFromObject(requestData));
	 * 
	 * // Verificamos la Firma Digital del dato recibido
	 * jwtService.validateDataSignature(requestData);
	 * 
	 * // decodificamos el dataRequest y obtenemos el Payload localidadesRequest
	 * localidadesRequest = (LocalidadesRequest) jwtService
	 * .getPayloadClassFromToken(requestData.getData(),
	 * localidadesRequest.class); log.info("__LocalidadesRequest:	" +
	 * Util.getJsonFromObject(LocalidadesRequest));
	 * 
	 * // obtenemos el usuario de SecurityContextHolder String usuariosys =
	 * SecurityContextHolder.getContext().getAuthentication().getName();
	 * 
	 * return securedResponse; }
	 */
}
