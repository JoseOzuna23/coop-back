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

import com.coop8.demojwt.Models.TiposPersonas;
import com.coop8.demojwt.PayloadModels.TiposPersonasResponseModel;
import com.coop8.demojwt.Repository.TiposPersonasRepository;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Request.TiposPersonasRequest;
import com.coop8.demojwt.Response.PaginacionResponse;
import com.coop8.demojwt.Response.Response;
import com.coop8.demojwt.Response.ResponseHeader;
import com.coop8.demojwt.Response.SecuredResponse;
import com.coop8.demojwt.Response.TiposPersonasResponse;
import com.coop8.demojwt.Utils.ECodigosRespuestas;
import com.coop8.demojwt.Utils.Util;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TiposPersonasService {

    @Autowired
    TiposPersonasRepository tiposPersonasRepository;

    @Autowired
    JwtService jwtService;

    /**
     * Busca y obtiene un {Optional<TiposPersonas>} en base al campo id
     * 
     * @param requestData
     * @return Optional<TiposPersonas>
     */
    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

        log.info("TiposPersonasService    | getById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // decodificamos el dataRequest y obtenemos el Payload
        TiposPersonasRequest tiposPersonasRequest = (TiposPersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposPersonasRequest.class);
        log.info("__tiposPersonasRequest:    " + Util.getJsonFromObject(tiposPersonasRequest));

        TiposPersonasResponse tiposPersonasResponse = new TiposPersonasResponse();

        long idTipoPersona = tiposPersonasRequest.getId();
        Optional<TiposPersonas> tipoPersona = tiposPersonasRepository.findById(idTipoPersona);

        if (tipoPersona.isPresent()) {
            List<TiposPersonas> tiposPersonasList = new ArrayList<TiposPersonas>();
            tiposPersonasList.add(tipoPersona.get());

            List<TiposPersonasResponseModel> tiposPersonasResponseModels = new ArrayList<>();

            for (TiposPersonas tiposPersonas : tiposPersonasList) {
                TiposPersonasResponseModel eu = new TiposPersonasResponseModel();
                eu.filterPayloadToSend(tiposPersonas);
                tiposPersonasResponseModels.add(eu);
            }

            tiposPersonasResponse.setTiposPersonas(tiposPersonasResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(tiposPersonasResponse);

        log.info("__TiposPersonasResponse:    " + Util.getJsonFromObject(tiposPersonasResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    /**
     * Método que retorna lista pageable de tiposPersonas
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("TiposPersonasService    | list");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // decodificamos el dataRequest y obtenemos el Payload
        TiposPersonasRequest tiposPersonasRequest = (TiposPersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposPersonasRequest.class);
        log.info("__tiposPersonasRequest:    " + Util.getJsonFromObject(tiposPersonasRequest));

        List<TiposPersonas> tiposPersonasList = new ArrayList<>();

        int pagina = tiposPersonasRequest.getPaginacion().getPagina() - 1;

        Pageable paging = PageRequest.of(pagina, tiposPersonasRequest.getPaginacion().getCantidad());

        Page<TiposPersonas> pageTiposPersonas;

        if (Util.isNullOrEmpty(tiposPersonasRequest.getDescripcion()))
            pageTiposPersonas = tiposPersonasRepository.findAllByOrderByDescripcionAsc(paging);
        else
            pageTiposPersonas = tiposPersonasRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
                    tiposPersonasRequest.getDescripcion(), paging);

        tiposPersonasList = pageTiposPersonas.getContent();

        List<TiposPersonasResponseModel> tiposPersonasResponseModels = new ArrayList<>();

        for (TiposPersonas tiposPersonas : tiposPersonasList) {
            TiposPersonasResponseModel eu = new TiposPersonasResponseModel();
                        eu.filterPayloadToSend(tiposPersonas);
            tiposPersonasResponseModels.add(eu);
        }

        TiposPersonasResponse tiposPersonasResponse = new TiposPersonasResponse();
        tiposPersonasResponse.setTiposPersonas(tiposPersonasResponseModels);
        PaginacionResponse pageableResponse = new PaginacionResponse();

        pageableResponse.setTotalItems(pageTiposPersonas.getTotalElements());
        pageableResponse.setTotalPages(pageTiposPersonas.getTotalPages());
        pageableResponse.setCurrentPages(pageTiposPersonas.getNumber() + 1);

        tiposPersonasResponse.setPaginacion(pageableResponse);

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(tiposPersonasResponse);

        log.info("__TiposPersonasResponse:    " + Util.getJsonFromObject(tiposPersonasResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }

    /**
     * Método que elimina un registro de la tabla tiposPersonas según el ID del
     * tipoPersona pasado por parámetro
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    @Transactional
    public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("TiposPersonasService    | deleteById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // decodificamos el dataRequest y obtenemos el Payload
        TiposPersonasRequest tiposPersonasRequest = (TiposPersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposPersonasRequest.class);
        log.info("__tiposPersonasRequest:    " + Util.getJsonFromObject(tiposPersonasRequest));

        long idTipoPersona = tiposPersonasRequest.getId();
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        tiposPersonasRepository.deleteById(idTipoPersona);
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
     * Método que Inserta un registro nuevo en la tabla tiposPersonas, o
     * actualiza un tipoPersona existente en ella. Esto según si el objeto recibido por
     * parámetro tiene o no un ID
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("TiposPersonasService    | save");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // decodificamos el dataRequest y obtenemos el Payload
        TiposPersonasRequest tiposPersonasRequest = (TiposPersonasRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposPersonasRequest.class);
        log.info("__tiposPersonasRequest:    " + Util.getJsonFromObject(tiposPersonasRequest));

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        // obtenemos el usuario de SecurityContextHolder
        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        // verificamos si es un INSERT o un UPDATE
        if (Util.isNullOrEmpty(tiposPersonasRequest.getId()) || tiposPersonasRequest.getId() == 0) {
            // INSERT
            TiposPersonas tipoPersonaInsert = new TiposPersonas();
            tipoPersonaInsert.setDescripcion(tiposPersonasRequest.getDescripcion());
            tipoPersonaInsert.setUsuariosys(usuariosys);

            tiposPersonasRepository.save(tipoPersonaInsert);
        } else {
            // UPDATE
            long idTipoPersona = tiposPersonasRequest.getId();

            TiposPersonas tipoPersonaUpdate = tiposPersonasRepository.findById(idTipoPersona).get();
            tipoPersonaUpdate.setDescripcion(tiposPersonasRequest.getDescripcion());
            tipoPersonaUpdate.setUsuariosys(usuariosys);

            tiposPersonasRepository.save(tipoPersonaUpdate);
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

           
