package com.coop8.demojwt.Service;

import com.coop8.demojwt.Jwt.JwtService;
import com.coop8.demojwt.Models.EstadosCiviles;
import com.coop8.demojwt.PayloadModels.EstadosCivilesResponseModel;
import com.coop8.demojwt.Repository.EstadosCivilesRepository;
import com.coop8.demojwt.Request.EstadosCivilesRequest;
import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Response.EstadosCivilesResponse;
import com.coop8.demojwt.Response.PaginacionResponse;
import com.coop8.demojwt.Response.Response;
import com.coop8.demojwt.Response.ResponseHeader;
import com.coop8.demojwt.Response.SecuredResponse;
import com.coop8.demojwt.Utils.ECodigosRespuestas;
import com.coop8.demojwt.Utils.Util;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EstadosCivilesService {

    private final EstadosCivilesRepository estadoCivilRepository;
    private final JwtService jwtService;

    @Autowired
    public EstadosCivilesService(EstadosCivilesRepository estadoCivilRepository, JwtService jwtService) {
        this.estadoCivilRepository = estadoCivilRepository;
        this.jwtService = jwtService;
    }

    public SecuredResponse getById(@Valid RequestData requestData) throws Exception {
        log.info("EstadosCivilesService | getById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosCivilesRequest estadoCivilesRequest = getRequestPayload(requestData);
        log.info("__estadoCivilesRequest: " + Util.getJsonFromObject(estadoCivilesRequest));

        Optional<EstadosCiviles> estadoCivil = estadoCivilRepository.findById(estadoCivilesRequest.getId());
        EstadosCivilesResponse estadoCivilResponse = new EstadosCivilesResponse();

        if (estadoCivil.isPresent()) {
            List<EstadosCivilesResponseModel> estadoCivilResponseModels = new ArrayList<>();
            estadoCivilResponseModels.add(filterPayload(estadoCivil.get()));
            estadoCivilResponse.setEstadosCiviles(estadoCivilResponseModels);
        }

        return buildSecuredResponse(estadoCivilResponse);
    }

    public SecuredResponse list(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("EstadosCivilesService | list");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosCivilesRequest estadoCivilesRequest = getRequestPayload(requestData);
        log.info("__estadoCivilesRequest: " + Util.getJsonFromObject(estadoCivilesRequest));

        Page<EstadosCiviles> pageEstadosCiviles = getPageableEstadosCiviles(estadoCivilesRequest);
        List<EstadosCivilesResponseModel> estadoCivilResponseModels = new ArrayList<>();

        for (EstadosCiviles estadoCivil : pageEstadosCiviles.getContent()) {
            estadoCivilResponseModels.add(filterPayload(estadoCivil));
        }

        EstadosCivilesResponse estadoCivilResponse = new EstadosCivilesResponse();
        estadoCivilResponse.setEstadosCiviles(estadoCivilResponseModels);
        estadoCivilResponse.setPaginacion(buildPaginacionResponse(pageEstadosCiviles));

        return buildSecuredResponse(estadoCivilResponse);
    }

    @Transactional
    public SecuredResponse deleteById(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("EstadosCivilesService | deleteById");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosCivilesRequest estadoCivilesRequest = getRequestPayload(requestData);
        log.info("__estadoCivilesRequest: " + Util.getJsonFromObject(estadoCivilesRequest));

        estadoCivilRepository.deleteById(estadoCivilesRequest.getId());

        return buildSecuredResponse(null);
    }

    public SecuredResponse save(@Valid @RequestBody RequestData requestData) throws Exception {
        log.info("EstadosCivilesService | save");
        log.info("__dataRequest: " + Util.getJsonFromObject(requestData));

        EstadosCivilesRequest estadoCivilesRequest = getRequestPayload(requestData);
        log.info("__estadoCivilesRequest: " + Util.getJsonFromObject(estadoCivilesRequest));

        String usuariosys = SecurityContextHolder.getContext().getAuthentication().getName();

        if (Util.isNullOrEmpty(estadoCivilesRequest.getId()) || estadoCivilesRequest.getId() == 0) {
            saveEstadoCivil(estadoCivilesRequest, usuariosys);
        } else {
            updateEstadoCivil(estadoCivilesRequest, usuariosys);
        }

        return buildSecuredResponse(null);
    }

    private EstadosCivilesRequest getRequestPayload(RequestData requestData) throws Exception {
        return (EstadosCivilesRequest) jwtService.getPayloadClassFromToken(requestData.getData(), EstadosCivilesRequest.class);
    }

    private EstadosCivilesResponseModel filterPayload(EstadosCiviles estadoCivil) {
        EstadosCivilesResponseModel responseModel = new EstadosCivilesResponseModel();
        responseModel.filterPayloadToSend(estadoCivil);
        return responseModel;
    }

    private Page<EstadosCiviles> getPageableEstadosCiviles(EstadosCivilesRequest request) {
        Pageable paging = PageRequest.of(request.getPaginacion().getPagina() - 1, request.getPaginacion().getCantidad());

        if (Util.isNullOrEmpty(request.getDescripcion())) {
            return estadoCivilRepository.findAllByOrderByDescripcionAsc(paging);
        } else {
            return estadoCivilRepository.findByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(request.getDescripcion(), paging);
        }
    }

    private PaginacionResponse buildPaginacionResponse(Page<EstadosCiviles> page) {
        PaginacionResponse response = new PaginacionResponse();
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setCurrentPages(page.getNumber() + 1);
        return response;
    }

    private SecuredResponse buildSecuredResponse(Object data) throws Exception {
        ResponseHeader header = new ResponseHeader();
        Response response = new Response();
        SecuredResponse securedResponse = new SecuredResponse();

        header.setCodResultado(ECodigosRespuestas.SUCCESS.getCodigoRespuesta());
        header.setTxtResultado(ECodigosRespuestas.SUCCESS.getTxtRespuesta());
        response.setHeader(header);
        response.setData(data);

        securedResponse.setData(jwtService.getDataFromPayload(response));
        log.info("__response: " + Util.getJsonFromObject(response));
        log.info("__securedResponse: " + Util.getJsonFromObject(securedResponse));

        return securedResponse;
    }

    private void saveEstadoCivil(EstadosCivilesRequest request, String usuariosys) {
        EstadosCiviles estadoCivil = new EstadosCiviles();
        estadoCivil.setDescripcion(request.getDescripcion());
        estadoCivil.setUsuariosys(usuariosys);
        estadoCivilRepository.save(estadoCivil);
    }

    private void updateEstadoCivil(EstadosCivilesRequest request, String usuariosys) {
        EstadosCiviles estadoCivil = estadoCivilRepository.findById(request.getId()).orElseThrow();
        estadoCivil.setDescripcion(request.getDescripcion());
        estadoCivil.setUsuariosys(usuariosys);
        estadoCivilRepository.save(estadoCivil);
    }
}
