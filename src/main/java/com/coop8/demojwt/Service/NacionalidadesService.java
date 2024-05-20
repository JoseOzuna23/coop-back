package com.coop8.demojwt.Service;

import com.coop8.demojwt.Jwt.JwtService;
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

import com.coop8.demojwt.Models.Nacionalidades;
import com.coop8.demojwt.PayloadModels.NacionalidadesResponseModel;
import com.coop8.demojwt.Repository.NacionalidadesRepository;
import com.coop8.demojwt.Request.NacionalidadesRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.NacionalidadesResponse;
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
public class NacionalidadesService {

    @Autowired
    NacionalidadesRepository nacionalidadesRepository;

    @Autowired
    JwtService jwtService;

    /**
     * Busca y obtiene un {Optional<Nacionalidades>} en base al campo id
     * 
     * @param requestData
     * @return Optional<Nacionalidades>
     */
    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

        log.info("NacionalidadesService    | getById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
        // Verificamos la Firma Digital del dato recibido
        // jwtService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        NacionalidadesRequest nacionalidadesRequest = (NacionalidadesRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), NacionalidadesRequest.class);
        log.info("__nacionalidadesRequest:    " + Util.getJsonFromObject(nacionalidadesRequest));

        NacionalidadesResponse nacionalidadesResponse = new NacionalidadesResponse();

        long idNacionalidad = nacionalidadesRequest.getId();
        Optional<Nacionalidades> nacionalidad = nacionalidadesRepository.findById(idNacionalidad);

        if (nacionalidad.isPresent()) {
            List<Nacionalidades> nacionalidadesList = new ArrayList<Nacionalidades>();
            nacionalidadesList.add(nacionalidad.get());

            List<NacionalidadesResponseModel> nacionalidadesResponseModels = new ArrayList<>();

            for (Nacionalidades nacionalidades : nacionalidadesList) {
                NacionalidadesResponseModel eu = new NacionalidadesResponseModel();
                eu.filterPayloadToSend(nacionalidades);
                nacionalidadesResponseModels.add(eu);
            }

            nacionalidadesResponse.setNacionalidades(nacionalidadesResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(nacionalidadesResponse);

        log.info("__NacionalidadesResponse:    " + Util.getJsonFromObject(nacionalidadesResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    /**
     * Método que retorna lista pageable de nacionalidades
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("NacionalidadesService    | list");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // Verificamos la Firma Digital del dato recibido
        // jwtService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        NacionalidadesRequest nacionalidadesRequest = (NacionalidadesRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), NacionalidadesRequest.class);
        log.info("__nacionalidadesRequest:    " + Util.getJsonFromObject(nacionalidadesRequest));

        List<Nacionalidades> nacionalidadesList = new ArrayList<>();

        int pagina = nacionalidadesRequest.getPaginacion().getPagina() - 1;

        Pageable paging = PageRequest.of(pagina, nacionalidadesRequest.getPaginacion().getCantidad());

        Page<Nacionalidades> pageNacionalidades;

        if (Util.isNullOrEmpty(nacionalidadesRequest.getDescripcion()))
            pageNacionalidades = nacionalidadesRepository.findAllByOrderByDescripcionAsc(paging);
        else
            pageNacionalidades = nacionalidadesRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
                    nacionalidadesRequest.getDescripcion(), paging);

        nacionalidadesList = pageNacionalidades.getContent();

        List<NacionalidadesResponseModel> nacionalidadesResponseModels = new ArrayList<>();

        for (Nacionalidades nacionalidades : nacionalidadesList) {
            NacionalidadesResponseModel eu = new NacionalidadesResponseModel();
            eu.filterPayloadToSend(nacionalidades);
            nacionalidadesResponseModels.add(eu);
        }

        NacionalidadesResponse nacionalidadesResponse = new NacionalidadesResponse();
        nacionalidadesResponse.setNacionalidades(nacionalidadesResponseModels);
        PaginacionResponse pageableResponse = new PaginacionResponse();

        pageableResponse.setTotalItems(pageNacionalidades.getTotalElements());
        pageableResponse.setTotalPages(pageNacionalidades.getTotalPages());
        pageableResponse.setCurrentPages(pageNacionalidades.getNumber() + 1);

        nacionalidadesResponse.setPaginacion(pageableResponse);

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(nacionalidadesResponse);

        log.info("__NacionalidadesResponse:    " + Util.getJsonFromObject(nacionalidadesResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }

    /**
     * Método que Inserta un registro nuevo en la tabla nacionalidades, o
     * actualiza un nacionalidad existente en ella. Esto según si el objeto recibido por
     * parámetro tiene o no un ID
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("NacionalidadesService    | save");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // Verificamos la Firma Digital del dato recibido
        // jwtService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        NacionalidadesRequest nacionalidadesRequest = (NacionalidadesRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), NacionalidadesRequest.class);
        log.info("__nacionalidadesRequest:    " + Util.getJsonFromObject(nacionalidadesRequest));

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        // obtenemos el usuario de SecurityContextHolder
        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        // verificamos si es un INSERT o un UPDATE
        if (Util.isNullOrEmpty(nacionalidadesRequest.getId()) || nacionalidadesRequest.getId() == 0) {
            // INSERT
            Nacionalidades nacionalidadInsert = new Nacionalidades();
            nacionalidadInsert.setDescripcion(nacionalidadesRequest.getDescripcion());
            nacionalidadInsert.setUsuariosys(usuariosys);

            nacionalidadesRepository.save(nacionalidadInsert);
        } else {
            // UPDATE
            long idNacionalidad = nacionalidadesRequest.getId();

            Nacionalidades nacionalidadUpdate = nacionalidadesRepository.findById(idNacionalidad).
get();
            nacionalidadUpdate.setDescripcion(nacionalidadesRequest.getDescripcion());
            nacionalidadUpdate.setUsuariosys(usuariosys);

            nacionalidadesRepository.save(nacionalidadUpdate);
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

    /**
     * Método que elimina un registro de la tabla nacionalidades según el ID del
     * nacionalidad pasado por parámetro
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    @Transactional
    public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("NacionalidadesService    | deleteById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
        // Verificamos la Firma Digital del dato recibido
        // jwtService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        NacionalidadesRequest nacionalidadesRequest = (NacionalidadesRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), NacionalidadesRequest.class);
        log.info("__nacionalidadesRequest:    " + Util.getJsonFromObject(nacionalidadesRequest));

        long idNacionalidad = nacionalidadesRequest.getId();
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        nacionalidadesRepository.deleteById(idNacionalidad);
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

