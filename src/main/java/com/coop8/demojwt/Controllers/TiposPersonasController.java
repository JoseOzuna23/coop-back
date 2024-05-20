package com.coop8.demojwt.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coop8.demojwt.Request.RequestData;
import com.coop8.demojwt.Service.TiposPersonasService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/referenciales/tiposPersonas")
@Slf4j
public class TiposPersonasController {
	String BaseUrlEndPoint;

	public TiposPersonasController() {
		BaseUrlEndPoint = "/referenciales/tiposPersonas";
		log.info("__BASE_end_point:	" + BaseUrlEndPoint);
	}

	@Autowired
	TiposPersonasService tiposPersonasService;

	@PostMapping("/list")
	public ResponseEntity<?> list(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("__end_point:	" + BaseUrlEndPoint + "/list");
		return new ResponseEntity<>(tiposPersonasService.list(requestData), HttpStatus.OK);
	}

	@PostMapping("/edit")
	public ResponseEntity<?> edit(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("__end_point:	" + BaseUrlEndPoint + "/edit");
		return new ResponseEntity<>(tiposPersonasService.getById(requestData), HttpStatus.OK);
	}

	@PostMapping("/getById")
	public ResponseEntity<?> getById(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("__end_point:	" + BaseUrlEndPoint + "/getById");
		return new ResponseEntity<>(tiposPersonasService.getById(requestData), HttpStatus.OK);
	}

	@PostMapping("/newAction")
	public ResponseEntity<?> newAction(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("__end_point:	" + BaseUrlEndPoint + "/newAction");
		return new ResponseEntity<>(tiposPersonasService.save(requestData), HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<?> save(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("__end_point:	" + BaseUrlEndPoint + "/save");
		return new ResponseEntity<>(tiposPersonasService.save(requestData), HttpStatus.OK);
	}

	@PostMapping("/deleteById")
	public ResponseEntity<?> deleteById(@Valid @RequestBody RequestData requestData) throws Exception {
		log.info("__end_point:	" + BaseUrlEndPoint + "/deleteById");
		return new ResponseEntity<>(tiposPersonasService.deleteById(requestData), HttpStatus.OK);
	}
}