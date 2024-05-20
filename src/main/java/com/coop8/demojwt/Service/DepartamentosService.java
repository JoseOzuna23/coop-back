package com.coop8.demojwt.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coop8.demojwt.Jwt.JwtService;
import com.coop8.demojwt.Models.Departamentos;
import com.coop8.demojwt.PayloadModels.DepartamentosResponseModel;
import com.coop8.demojwt.Repository.DepartamentosRepository;
import com.coop8.demojwt.Request.DepartamentosRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.DepartamentosResponse;
import com.coop8.demojwt.Response.PaginacionResponse;
import com.coop8.demojwt.Response.Response;
import com.coop8.demojwt.Response.ResponseHeader;
import com.coop8.demojwt.Response.SecuredResponse;
import com.coop8.demojwt.Utils.ECodigosRespuestas;
import com.coop8.demojwt.Utils.Util;
import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class DepartamentosService {

    @Autowired
    DepartamentosRepository departamentosRepository;

    @Autowired
    JwtService jwtService;

    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {

        log.info("DepartamentosService | getById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));
        
        // Decodificamos el dataRequest y obtenemos el Payload
        DepartamentosRequest departamentosRequest = jwtService.getClaim(requestData.getData(), claims -> {
            return claims.get("departamentosRequest", DepartamentosRequest.class);
        });
        
        log.info("__departamentosRequest: " + Util.getJsonFromObject(departamentosRequest));

        DepartamentosResponse departamentosResponse = new DepartamentosResponse();

        long idDepartamento = departamentosRequest.getId();
        Optional<Departamentos> departamento = departamentosRepository.findById(idDepartamento);

        if (departamento.isPresent()) {
            List<Departamentos> departamentosList = new ArrayList<Departamentos>();
            departamentosList.add(departamento.get());

            List<DepartamentosResponseModel> departamentosResponseModels = new ArrayList<>();

            for (Departamentos departamentos : departamentosList) {
                DepartamentosResponseModel eu = new DepartamentosResponseModel();
                eu.filterPayloadToSend(departamentos);
                departamentosResponseModels.add(eu);
            }

            departamentosResponse.setDepartamentos(departamentosResponseModels);
        }

        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(departamentosResponse);

        log.info("__DepartamentosResponse: " + Util.getJsonFromObject(departamentosResponse));
        
        // Creating the token with extra claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("response", response);
        securedResponse.setData(jwtService.getToken(claims));
        
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

/**
 * Método que retorna lista pageable de departamentos
 * 
 * @param requestData
 * @return {SecuredResponse}
 * @throws Exception
 */
public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {

    log.info("DepartamentosService | list");
    log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

    // Decodificamos el dataRequest y obtenemos el Payload
    DepartamentosRequest departamentosRequest = jwtService.getClaim(requestData.getData(), claims -> {
        return claims.get("departamentosRequest", DepartamentosRequest.class);
    });
    log.info("__departamentosRequest: " + Util.getJsonFromObject(departamentosRequest));

    List<Departamentos> departamentosList = new ArrayList<>();

    int pagina = departamentosRequest.getPaginacion().getPagina() - 1;

    Pageable paging = PageRequest.of(pagina, departamentosRequest.getPaginacion().getCantidad());

    Page<Departamentos> pageDepartamentos;

    if (Util.isNullOrEmpty(departamentosRequest.getDescripcion()))
        pageDepartamentos = departamentosRepository.findAllByOrderByDescripcionAsc(paging);
    else
        pageDepartamentos = departamentosRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(
                departamentosRequest.getDescripcion(), paging);

    departamentosList = pageDepartamentos.getContent();

    List<DepartamentosResponseModel> departamentosResponseModels = new ArrayList<>();

    for (Departamentos departamentos : departamentosList) {
        DepartamentosResponseModel eu = new DepartamentosResponseModel();
        eu.filterPayloadToSend(departamentos);
        departamentosResponseModels.add(eu);
    }

    DepartamentosResponse departamentosResponse = new DepartamentosResponse();
    departamentosResponse.setDepartamentos(departamentosResponseModels);
    PaginacionResponse pageableResponse = new PaginacionResponse();

    pageableResponse.setTotalItems(pageDepartamentos.getTotalElements());
    pageableResponse.setTotalPages(pageDepartamentos.getTotalPages());
    pageableResponse.setCurrentPages(pageDepartamentos.getNumber() + 1);

    departamentosResponse.setPaginacion(pageableResponse);

    ResponseHeader header = new ResponseHeader();
    Response response = new Response();
    SecuredResponse securedResponse = new SecuredResponse();

    header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
    header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
    response.setHeader(header);
    response.setData(departamentosResponse);

    log.info("__DepartamentosResponse: " + Util.getJsonFromObject(departamentosResponse));

    // Creating the token with extra claims
    Map<String, Object> claims = new HashMap<>();
    claims.put("response", response);
    securedResponse.setData(jwtService.getToken(claims));

    log.info("__response: " + Util.getJsonFromObject(response));
    log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

    return securedResponse;
}

/**
 * Método que elimina un registro de la tabla departamentos según el ID del
 * departamento pasado por parámetro
 * 
 * @param requestData
 * @return {SecuredResponse}
 * @throws Exception
 */
@Transactional
public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {

    log.info("DepartamentosService | deleteById");
    log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

    // Decodificamos el dataRequest y obtenemos el Payload
    DepartamentosRequest departamentosRequest = jwtService.getClaim(requestData.getData(), claims -> {
        return claims.get("departamentosRequest", DepartamentosRequest.class);
    });
    log.info("__departamentosRequest: " + Util.getJsonFromObject(departamentosRequest));

    long idDepartamento = departamentosRequest.getId();
    ResponseHeader header = new ResponseHeader();
    Response response = new Response();
    SecuredResponse securedResponse = new SecuredResponse();

    departamentosRepository.deleteById(idDepartamento);
    header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
    header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
    response.setHeader(header);
    response.setData(null);

    // Creating the token with extra claims
    Map<String, Object> claims = new HashMap<>();
    claims.put("response", response);
    securedResponse.setData(jwtService.getToken(claims));

    log.info("__response: " + Util.getJsonFromObject(response));
    log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

    return securedResponse;
}

/**
 * Método que Inserta un registro nuevo en la tabla departamentos, o
 * actualiza un departamento existente en ella. Esto según si el objeto recibido por
 * parámetro tiene o no un ID
 * 
 * @param requestData
 * @return {SecuredResponse}
 * @throws Exception
 */
public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
    log.info("DepartamentosService | save");
    log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

    // Decodificamos el dataRequest y obtenemos el Payload
    DepartamentosRequest departamentosRequest = jwtService.getClaim(requestData.getData(), claims -> {
        return claims.get("departamentosRequest", DepartamentosRequest.class);
    });
    log.info("__departamentosRequest: " + Util.getJsonFromObject(departamentosRequest));

    ResponseHeader header = new ResponseHeader();
    Response response = new Response();
    SecuredResponse securedResponse = new SecuredResponse();

    // Obtenemos el usuario de SecurityContextHolder
    String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

    // Verificamos si es un INSERT o un UPDATE
    if (Util.isNullOrEmpty(departamentosRequest.getId()) || departamentosRequest.getId() == 0) {
        // INSERT
        Departamentos departamentoInsert = new Departamentos();
        departamentoInsert.setDescripcion(departamentosRequest.getDescripcion().toUpperCase());
        departamentoInsert.setFechasys(new Date());
        departamentoInsert.setUsuariosys(usuariosys);

        departamentosRepository.save(departamentoInsert);
    } else {
        // UPDATE
        long idDepartamento = departamentosRequest.getId();

        Departamentos departamentoUpdate = departamentosRepository.findById(idDepartamento).get();
        departamentoUpdate.setDescripcion(departamentosRequest.getDescripcion().toUpperCase());
        departamentoUpdate.setFechasys(new Date());
        departamentoUpdate.setUsuariosys(usuariosys);

        departamentosRepository.save(departamentoUpdate);
    }

    header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
    header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
    response.setHeader(header);
    response.setData(null);

    // Creating the token with extra claims
    Map<String, Object> claims = new HashMap<>();
    claims.put("response", response);
    securedResponse.setData(jwtService.getToken(claims));

    log.info("__response: " + Util.getJsonFromObject(response));
    log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));
    return securedResponse;
}

}