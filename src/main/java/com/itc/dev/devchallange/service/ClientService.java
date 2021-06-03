package com.itc.dev.devchallange.service;

import java.util.List;

import com.itc.dev.devchallange.entity.Client;

public interface ClientService {
	public List<Client> fetch(String firstName, String mobileNumber, String idNumber);

	public Client create(Client client);
	
	public Client update(String idNumber,Client client);
}
