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

import com.coop8.demojwt.Models.TiposDocumentos;
import com.coop8.demojwt.PayloadModels.TiposDocumentosResponseModel;
import com.coop8.demojwt.Repository.TiposDocumentosRepository;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Request.TiposDocumentosRequest;
import com.coop8.demojwt.Response.PaginacionResponse;
import com.coop8.demojwt.Response.Response;
import com.coop8.demojwt.Response.ResponseHeader;
import com.coop8.demojwt.Response.SecuredResponse;
import com.coop8.demojwt.Response.TiposDocumentosResponse;
import com.coop8.demojwt.Utils.ECodigosRespuestas;
import com.coop8.demojwt.Utils.Util;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TiposDocumentosService {

    @Autowired
    TiposDocumentosRepository tiposDocumentosRepository;

    @Autowired
    JwtService jwtService;

    /**
     * Busca y obtiene un {Optional<TiposDocumentos>} en base al campo id
     * 
     * @param requestData
     * @return Optional<TiposDocumentos>
     */
    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

        log.info("TiposDocumentosService    | getById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        TiposDocumentosRequest tiposDocumentosRequest = (TiposDocumentosRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposDocumentosRequest.class);
        log.info("__tiposDocumentosRequest:    " + Util.getJsonFromObject(tiposDocumentosRequest));

        TiposDocumentosResponse tiposDocumentosResponse = new TiposDocumentosResponse();

        long idTipoDocumento = tiposDocumentosRequest.getId();
        Optional<TiposDocumentos> tipoDocumento = tiposDocumentosRepository.findById(idTipoDocumento);

        if (tipoDocumento.isPresent()) {
            List<TiposDocumentos> tiposDocumentosList = new ArrayList<TiposDocumentos>();
            tiposDocumentosList.add(tipoDocumento.get());

            List<TiposDocumentosResponseModel> tiposDocumentosResponseModels = new ArrayList<>();

            for (TiposDocumentos tiposDocumentos : tiposDocumentosList) {
                TiposDocumentosResponseModel eu = new TiposDocumentosResponseModel();
                eu.filterPayloadToSend(tiposDocumentos);
                tiposDocumentosResponseModels.add(eu);
            }

            tiposDocumentosResponse.setTiposDocumentos(tiposDocumentosResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(tiposDocumentosResponse);

        log.info("__TiposDocumentosResponse:    " + Util.getJsonFromObject(tiposDocumentosResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    /**
     * Método que retorna lista pageable de tiposDocumentos
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("TiposDocumentosService    | list");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        TiposDocumentosRequest tiposDocumentosRequest = (TiposDocumentosRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposDocumentosRequest.class);
        log.info("__tiposDocumentosRequest:    " + Util.getJsonFromObject(tiposDocumentosRequest));

        List<TiposDocumentos> tiposDocumentosList = new ArrayList<>();

        int pagina = tiposDocumentosRequest.getPaginacion().getPagina() - 1;

        Pageable paging = PageRequest.of(pagina, tiposDocumentosRequest.getPaginacion().getCantidad());

        Page<TiposDocumentos> pageTiposDocumentos;

        if (Util.isNullOrEmpty(tiposDocumentosRequest.getDescripcion()))
            pageTiposDocumentos = tiposDocumentosRepository.findAllByOrderByDescripcionAsc(paging);
        else
            pageTiposDocumentos = tiposDocumentosRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
                    tiposDocumentosRequest.getDescripcion(), paging);

        tiposDocumentosList = pageTiposDocumentos.getContent();

        List<TiposDocumentosResponseModel> tiposDocumentosResponseModels = new ArrayList<>();

        for (TiposDocumentos tiposDocumentos : tiposDocumentosList) {
            TiposDocumentosResponseModel eu = new TiposDocumentosResponseModel();
            eu.filterPayloadToSend(tiposDocumentos);
            tiposDocumentosResponseModels.add(eu);
        }

        TiposDocumentosResponse tiposDocumentosResponse = new TiposDocumentosResponse();
        tiposDocumentosResponse.setTiposDocumentos(tiposDocumentosResponseModels);
        PaginacionResponse pageableResponse = new PaginacionResponse();

        pageableResponse.setTotalItems(pageTiposDocumentos.getTotalElements());
        pageableResponse.setTotalPages(pageTiposDocumentos.getTotalPages());
        pageableResponse.setCurrentPages(pageTiposDocumentos.getNumber() + 1);

        tiposDocumentosResponse.setPaginacion(pageableResponse);

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(tiposDocumentosResponse);

        log.info("__TiposDocumentosResponse:    " + Util.getJsonFromObject(tiposDocumentosResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response:    " + Util.getJsonFromObject(response));
        log.info("__securedResponse:    " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }

        public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("TiposDocumentosService    | deleteById");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        TiposDocumentosRequest tiposDocumentosRequest = (TiposDocumentosRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposDocumentosRequest.class);
        log.info("__tiposDocumentosRequest:    " + Util.getJsonFromObject(tiposDocumentosRequest));

        long idTipoDocumento = tiposDocumentosRequest.getId();
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        tiposDocumentosRepository.deleteById(idTipoDocumento);
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
     * Método que Inserta un registro nuevo en la tabla tiposDocumentos, o actualiza un tipoDocumento existente en ella.
     * Esto según si el objeto recibido por parámetro tiene o no un ID
     * 
     * @param requestData
     * @return {SecuredResponse}
     * @throws Exception
     */
    public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("TiposDocumentosService    | save");
        log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));

        // Verificamos la Firma Digital del dato recibido
        // arapyApiSecurityService.validateDataSignature(requestData);

        // decodificamos el dataRequest y obtenemos el Payload
        TiposDocumentosRequest tiposDocumentosRequest = (TiposDocumentosRequest) jwtService
                .getPayloadClassFromToken(requestData.getData(), TiposDocumentosRequest.class);
        log.info("__tiposDocumentosRequest:    " + Util.getJsonFromObject(tiposDocumentosRequest));

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        // obtenemos el usuario de SecurityContextHolder
        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        // verificamos si es un INSERT o un UPDATE
        if (Util.isNullOrEmpty(tiposDocumentosRequest.getId()) || tiposDocumentosRequest.getId() == 0) {
            // INSERT
            TiposDocumentos tipoDocumentoInsert = new TiposDocumentos();
            tipoDocumentoInsert.setDescripcion(tiposDocumentosRequest.getDescripcion());
            tipoDocumentoInsert.setUsuariosys(usuariosys);

            tiposDocumentosRepository.save(tipoDocumentoInsert);
        } else {
            // UPDATE
            long idTipoDocumento = tiposDocumentosRequest.getId();

            TiposDocumentos tipoDocumentoUpdate = tiposDocumentosRepository.findById(idTipoDocumento).get();
            tipoDocumentoUpdate.setDescripcion(tiposDocumentosRequest.getDescripcion());
            tipoDocumentoUpdate.setUsuariosys(usuariosys);

            tiposDocumentosRepository.save(tipoDocumentoUpdate);
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

    /*
     * public SecuredResponse nuevo(@Valid @RequestBody RequestData requestData)
     * throws Exception { log.info("TiposDocumentosService    | nuevo");
     * log.info("__dataRequest:    " + Util.getJsonFromObject(requestData));
     * 
     * // Verificamos la Firma Digital del dato recibido
     * arapyApiSecurityService.validateDataSignature(requestData);
     * 
     * // decodificamos el dataRequest y obtenemos el Payload tiposDocumentosRequest
     * tiposDocumentosRequest = (TiposDocumentosRequest) arapyApiSecurityService
     * .getPayloadClassFromToken(requestData.getData(),
     * tiposDocumentosRequest.class); log.info("__TiposDocumentosRequest:    " +
     * Util.getJsonFromObject(TiposDocumentosRequest));
     * 
     * // obtenemos el usuario de SecurityContextHolder String usuariosys =
     * SecurityContextHolder.getContext().getAuthentication().getName();
     * 
     * return securedResponse; }
     */
}

