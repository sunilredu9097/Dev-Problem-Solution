package com.itc.dev.devchallange.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itc.dev.devchallange.entity.Client;
import com.itc.dev.devchallange.service.ClientService;

@RestController
@RequestMapping("/client/api/v1/")
@Validated
public class ClientController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
		LOGGER.info("ClientController: createClient API Invoked");
		Client createdClient = clientService.create(client);
		return new ResponseEntity<Client>(createdClient, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Client>> getClient(@RequestParam(required = false) String firstName,
			@Pattern(regexp = "(0/91)?[7-9][0-9]{9}") @RequestParam(required = false) String mobileNumber,
			@RequestParam(required = false) String idNumber) {
		LOGGER.info("ClientController: getClient API Invoked");
		List<Client> clients = clientService.fetch(firstName, mobileNumber, idNumber);
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}

	@PutMapping(value = "{idNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Client> updateClient(@PathVariable @NotNull String idNumber, @RequestBody Client client) {
		LOGGER.info("ClientController: createClient API Invoked");
		Client updatedClient = clientService.update(idNumber, client);
		return new ResponseEntity<Client>(updatedClient, HttpStatus.NO_CONTENT);
	}
}
