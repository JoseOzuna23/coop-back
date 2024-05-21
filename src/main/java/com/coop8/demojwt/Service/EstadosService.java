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

import com.coop8.demojwt.Models.Estados;
import com.coop8.demojwt.PayloadModels.EstadosResponseModel;
import com.coop8.demojwt.Repository.EstadosRepository;
import com.coop8.demojwt.Request.EstadosRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.EstadosResponse;
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
public class EstadosService {

    @Autowired
    EstadosRepository estadosRepository;

    @Autowired
    JwtService jwtService;

    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

        log.info("EstadosService | getById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosRequest estadosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosRequest.class);
        log.info("__estadosRequest: " + Util.getJsonFromObject(estadosRequest));

        EstadosResponse estadosResponse = new EstadosResponse();

        long idEstado = estadosRequest.getId();
        Optional<Estados> estado = estadosRepository.findById(idEstado);

        if (estado.isPresent()) {
            List<Estados> estadosList = new ArrayList<Estados>();
            estadosList.add(estado.get());

            List<EstadosResponseModel> estadosResponseModels = new ArrayList<>();

            for (Estados estados : estadosList) {
                EstadosResponseModel eu = new EstadosResponseModel();
                eu.filterPayloadToSend(estados);
                estadosResponseModels.add(eu);
            }

            estadosResponse.setEstados(estadosResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(estadosResponse);

        log.info("__EstadosResponse: " + Util.getJsonFromObject(estadosResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("EstadosService | list");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosRequest estadosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosRequest.class);
        log.info("__estadosRequest: " + Util.getJsonFromObject(estadosRequest));

        List<Estados> estadosList = new ArrayList<>();

        int pagina = estadosRequest.getPaginacion().getPagina() - 1;

        Pageable paging = PageRequest.of(pagina, estadosRequest.getPaginacion().getCantidad());

        Page<Estados> pageEstados;

        if (Util.isNullOrEmpty(estadosRequest.getDescripcion()))
            pageEstados = estadosRepository.findAllByOrderByDescripcionAsc(paging);
        else
            pageEstados = estadosRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
                    estadosRequest.getDescripcion(), paging);

        estadosList = pageEstados.getContent();

        List<EstadosResponseModel> estadosResponseModels = new ArrayList<>();

        for (Estados estados : estadosList) {
            EstadosResponseModel eu = new EstadosResponseModel();
            eu.filterPayloadToSend(estados);
            estadosResponseModels.add(eu);
        }

        EstadosResponse estadosResponse = new EstadosResponse();
        estadosResponse.setEstados(estadosResponseModels);
        PaginacionResponse pageableResponse = new PaginacionResponse();

        pageableResponse.setTotalItems(pageEstados.getTotalElements());
        pageableResponse.setTotalPages(pageEstados.getTotalPages());
        pageableResponse.setCurrentPages(pageEstados.getNumber() + 1);

        estadosResponse.setPaginacion(pageableResponse);

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(estadosResponse);

        log.info("__EstadosResponse: " + Util.getJsonFromObject(estadosResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }

    @Transactional
    public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

        log.info("EstadosService | deleteById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosRequest estadosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosRequest.class);
        log.info("__estadosRequest: " + Util.getJsonFromObject(estadosRequest));

        long idEstado = estadosRequest.getId();
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        estadosRepository.deleteById(idEstado);
        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(null);

        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("EstadosService | save");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosRequest estadosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosRequest.class);
        log.info("__estadosRequest: " + Util.getJsonFromObject(estadosRequest));

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        if (Util.isNullOrEmpty(estadosRequest.getId()) || estadosRequest.getId() == 0) {
            Estados estadoInsert = new Estados();
            estadoInsert.setDescripcion(estadosRequest.getDescripcion());
            estadoInsert.setUsuariosys(usuariosys);

			estadosRepository.save(estadoInsert);
        } else {
            long idEstado = estadosRequest.getId();
            Estados estadoUpdate = estadosRepository.findById(idEstado).get();
            estadoUpdate.setDescripcion(estadosRequest.getDescripcion());
            estadoUpdate.setUsuariosys(usuariosys);
            estadosRepository.save(estadoUpdate);
        }

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(null);

        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));
        return securedResponse;
    }
}