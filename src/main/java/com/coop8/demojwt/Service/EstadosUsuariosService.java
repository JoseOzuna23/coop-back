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
import com.coop8.demojwt.Models.EstadosUsuarios;
import com.coop8.demojwt.PayloadModels.EstadosUsuariosResponseModel;
import com.coop8.demojwt.Repository.EstadosUsuariosRepository;
import com.coop8.demojwt.Request.EstadosUsuariosRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.EstadosUsuariosResponse;
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
public class EstadosUsuariosService {

    @Autowired
    EstadosUsuariosRepository estadosUsuariosRepository;

    @Autowired
    JwtService jwtService;

    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {
        log.info("EstadosUsuariosService | getById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosUsuariosRequest estadosUsuariosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosUsuariosRequest.class);
        log.info("__estadosUsuariosRequest: " + Util.getJsonFromObject(estadosUsuariosRequest));

        EstadosUsuariosResponse estadosUsuariosResponse = new EstadosUsuariosResponse();
        long idEstadoUsuario = estadosUsuariosRequest.getId();
        Optional<EstadosUsuarios> estadoUsuario = estadosUsuariosRepository.findById(idEstadoUsuario);

        if (estadoUsuario.isPresent()) {
            List<EstadosUsuarios> estadosUsuariosList = new ArrayList<>();
            estadosUsuariosList.add(estadoUsuario.get());

            List<EstadosUsuariosResponseModel> estadosUsuariosResponseModels = new ArrayList<>();
            for (EstadosUsuarios estadoUusuario : estadosUsuariosList) {
                EstadosUsuariosResponseModel eu = new EstadosUsuariosResponseModel();
                eu.filterPayloadToSend(estadoUusuario);
                estadosUsuariosResponseModels.add(eu);
            }

            estadosUsuariosResponse.setEstadosUsuarios(estadosUsuariosResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(estadosUsuariosResponse);

        log.info("__estadosUsuariosResponse: " + Util.getJsonFromObject(estadosUsuariosResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("EstadosUsuariosService | list");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosUsuariosRequest estadosUsuariosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosUsuariosRequest.class);
        log.info("__estadosUsuariosRequest: " + Util.getJsonFromObject(estadosUsuariosRequest));

        List<EstadosUsuarios> estadosUsuariosList = new ArrayList<>();
        int pagina = estadosUsuariosRequest.getPaginacion().getPagina() - 1;
        Pageable paging = PageRequest.of(pagina, estadosUsuariosRequest.getPaginacion().getCantidad());
        Page<EstadosUsuarios> pageEstadosUsuarios;

        if (Util.isNullOrEmpty(estadosUsuariosRequest.getDescripcion())) {
            pageEstadosUsuarios = estadosUsuariosRepository.findAllByOrderByDescripcionAsc(paging);
        } else {
            pageEstadosUsuarios = estadosUsuariosRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
                estadosUsuariosRequest.getDescripcion(), paging);
        }

        estadosUsuariosList = pageEstadosUsuarios.getContent();
        List<EstadosUsuariosResponseModel> estadosUsuariosResponseModels = new ArrayList<>();

        for (EstadosUsuarios estadoUusuario : estadosUsuariosList) {
            EstadosUsuariosResponseModel eu = new EstadosUsuariosResponseModel();
            eu.filterPayloadToSend(estadoUusuario);
            estadosUsuariosResponseModels.add(eu);
        }

        EstadosUsuariosResponse estadosUsuariosResponse = new EstadosUsuariosResponse();
        estadosUsuariosResponse.setEstadosUsuarios(estadosUsuariosResponseModels);
        PaginacionResponse pageableResponse = new PaginacionResponse();

        pageableResponse.setTotalItems(pageEstadosUsuarios.getTotalElements());
        pageableResponse.setTotalPages(pageEstadosUsuarios.getTotalPages());
        pageableResponse.setCurrentPages(pageEstadosUsuarios.getNumber() + 1);

        estadosUsuariosResponse.setPaginacion(pageableResponse);

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(estadosUsuariosResponse);

        log.info("__estadosUsuariosResponse: " + Util.getJsonFromObject(estadosUsuariosResponse));
        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    @Transactional
    public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("EstadosUsuariosService | deleteById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosUsuariosRequest estadosUsuariosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosUsuariosRequest.class);
        log.info("__estadosUsuariosRequest: " + Util.getJsonFromObject(estadosUsuariosRequest));

        long idEstadoUsuario = estadosUsuariosRequest.getId();
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        estadosUsuariosRepository.deleteById(idEstadoUsuario);
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
        log.info("EstadosUsuariosService | save");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosUsuariosRequest estadosUsuariosRequest = jwtService.getPayloadClassFromToken(requestData.getData(), EstadosUsuariosRequest.class);
        log.info("__estadosUsuariosRequest: " + Util.getJsonFromObject(estadosUsuariosRequest));

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        if (Util.isNullOrEmpty(estadosUsuariosRequest.getId()) || estadosUsuariosRequest.getId() == 0) {
            EstadosUsuarios estadoUsuarioInsert = new EstadosUsuarios();
            estadoUsuarioInsert.setDescripcion(estadosUsuariosRequest.getDescripcion());
            estadoUsuarioInsert.setUsuariosys(usuariosys);
            estadosUsuariosRepository.save(estadoUsuarioInsert);
        } else {
            long idEstadoUsuario = estadosUsuariosRequest.getId();
            EstadosUsuarios estadoUsuarioUpdate = estadosUsuariosRepository.findById(idEstadoUsuario).get();
            estadoUsuarioUpdate.setDescripcion(estadosUsuariosRequest.getDescripcion());
            estadoUsuarioUpdate.setUsuariosys(usuariosys);
            estadosUsuariosRepository.save(estadoUsuarioUpdate);
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
