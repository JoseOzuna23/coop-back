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

import com.coop8.demojwt.Models.Personas;
import com.coop8.demojwt.PayloadModels.PersonasResponseModel;
import com.coop8.demojwt.Repository.PersonasRepository;
import com.coop8.demojwt.Request.PersonasRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.PaginacionResponse;
import com.coop8.demojwt.Response.PersonasResponse;
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
public class PersonasService {

    @Autowired
    PersonasRepository personasRepository;

    @Autowired
    JwtService jwtService;

    /**
     * Busca y obtiene un {Optional<Personas>} en base al campo id
     * 
     * @param requestData
     * @return Optional<Personas>
     */
    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

        log.info("PersonasService    | getById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        PersonasRequest personasRequest = (PersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), PersonasRequest.class);
        log.info("__personasRequest:    " + Util.getJsonFromObject(personasRequest));

        PersonasResponse personasResponse = new PersonasResponse();

        long idPersona = personasRequest.getId();
        Optional<Personas> persona = personasRepository.findById(idPersona);

        if (persona.isPresent()) {
            List<Personas> personasList = new ArrayList<Personas>();
            personasList.add(persona.get());

            List<PersonasResponseModel> personasResponseModels = new ArrayList<>();

            for (Personas personas : personasList) {
                PersonasResponseModel eu = new PersonasResponseModel();
                eu.filterPayloadToSend(personas);
                personasResponseModels.add(eu);
            }

            personasResponse.setPersonas(personasResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(personasResponse);

        log.info("__PersonasResponse:    " + Util.getJsonFromObject(personasResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    /**
     * Método que retorna lista pageable de personas
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("PersonasService    | list");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        PersonasRequest personasRequest = (PersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), PersonasRequest.class);
        log.info("__personasRequest:    " + Util.getJsonFromObject(personasRequest));

        List<Personas> personasList = new ArrayList<>();

        int pagina = personasRequest.getPaginacion().getPagina() - 1;

        Pageable paging = PageRequest.of(pagina, personasRequest.getPaginacion().getCantidad());

        Page<Personas> pagePersonas;

        if (Util.isNullOrEmpty(personasRequest.getNroDocumento()))
            pagePersonas = personasRepository.findAllByOrderByNroDocumentoAsc(paging);
        else
            pagePersonas = personasRepository.findByNroDocumentoContainingIgnoreCaseOrderByNroDocumentoAsc(
                    personasRequest.getNroDocumento(), paging);

        personasList = pagePersonas.getContent();

        List<PersonasResponseModel> personasResponseModels = new ArrayList<>();

        for (Personas personas : personasList) {
            PersonasResponseModel eu = new PersonasResponseModel();
            eu.filterPayloadToSend(personas);
            personasResponseModels.add(eu);
        }

        PersonasResponse personasResponse = new PersonasResponse();
        personasResponse.setPersonas(personasResponseModels);
        PaginacionResponse pageableResponse = new PaginacionResponse();

        pageableResponse.setTotalItems(pagePersonas.getTotalElements());
        pageableResponse.setTotalPages(pagePersonas.getTotalPages());
        pageableResponse.setCurrentPages(pagePersonas.getNumber() + 1);

        personasResponse.setPaginacion(pageableResponse);

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(personasResponse);

        log.info("__PersonasResponse:    " + Util.getJsonFromObject(personasResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }

    /**
     * Método que elimina un registro de la tabla personas según el ID del
     * persona pasado por parámetro
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    @Transactional
    public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("PersonasService    | deleteById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        PersonasRequest personasRequest = (PersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), PersonasRequest.class);
        log.info("__personasRequest:    " + Util.getJsonFromObject(personasRequest));

       
        long idPersona = personasRequest.getId();
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        personasRepository.deleteById(idPersona);
        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(null);

        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    /**
     * Método que Inserta un registro nuevo en la tabla personas, o
     * actualiza un persona existente en ella. Esto según si el objeto recibido por
     * parámetro tiene o no un ID
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("PersonasService    | save");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        PersonasRequest personasRequest = (PersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), PersonasRequest.class);
        log.info("__personasRequest:    " + Util.getJsonFromObject(personasRequest));

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        // obtenemos el usuario de SecurityContextHolder
        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        // verificamos si es un INSERT o un UPDATE
        if (Util.isNullOrEmpty(personasRequest.getId()) || personasRequest.getId() == 0) {
            // INSERT
            Personas personaInsert = new Personas();
            personaInsert.setNombre1(personasRequest.getNombre1().toUpperCase());
            personaInsert.setNombre2(personasRequest.getNombre2().toUpperCase());
            personaInsert.setNombre3(personasRequest.getNombre3().toUpperCase());
            personaInsert.setApellido1(personasRequest.getApellido1().toUpperCase());
            personaInsert.setApellido2(personasRequest.getApellido2().toUpperCase());
            personaInsert.setApellido3(personasRequest.getApellido3().toUpperCase());
            personaInsert.setSexo(personasRequest.getSexo().toUpperCase());
            personaInsert.setFechasys(new Date());
            personaInsert.setUsuariosys(usuariosys);

            personasRepository.save(personaInsert);
        } else {
            // UPDATE
            long idPersona = personasRequest.getId();

            Personas personaUpdate = personasRepository.findById(idPersona).get();
            personaUpdate.setNombre1(personasRequest.getNombre1().toUpperCase());
            personaUpdate.setNombre2(personasRequest.getNombre2().toUpperCase());
            personaUpdate.setNombre3(personasRequest.getNombre3().toUpperCase());
            personaUpdate.setApellido1(personasRequest.getApellido1().toUpperCase());
            personaUpdate.setApellido2(personasRequest.getApellido2().toUpperCase());
            personaUpdate.setApellido3(personasRequest.getApellido3().toUpperCase());
            personaUpdate.setSexo(personasRequest.getSexo().toUpperCase());
            personaUpdate.setFechasys(new Date());
            personaUpdate.setUsuariosys(usuariosys);

            personasRepository.save(personaUpdate);
        }

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(null);

        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }
}
