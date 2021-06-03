package com.itc.dev.devchallange.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.itc.dev.devchallange.entity.Address;
import com.itc.dev.devchallange.entity.Client;

@Repository
public class ClientRepository {
	static Map<String, Client> clientMap = new HashMap<String, Client>();
	static int count = 0;
	static {

		/*
		 * clientMap.put(count++, new Client("AB", "de Villiers", "8767976536",
		 * "4001014800084", new Address(" Bela-Bela", "South Africa", "Line 1", "Line2")));
		 */
		clientMap.put("7801014800084", new Client("JP", "Dumini", "8754565356", "7801014800084",
				new Address("Durban", "South Africa", "Line 1", "Line 2")));

		/*
		 * clientMap.put("", ""); clientMap.put("", ""); clientMap.put("", "");
		 */
	}
	
	public List<Client> findByNameOrMobileNumberOIdnumber(String search) {
		return clientMap.entrySet().stream().filter(v -> search.equalsIgnoreCase(v.getValue().getFirstName())
				|| search.equalsIgnoreCase(v.getValue().getMobileNumber())
				|| search.equalsIgnoreCase(v.getValue().getIdNumber())
				)
				.map(x -> x.getValue()).collect(Collectors.toList());
	}

	public List<Client> findByFirstName(String firstName) {
		return clientMap.entrySet().stream().filter(v -> firstName.equalsIgnoreCase(v.getValue().getFirstName()))
				.map(x -> x.getValue()).collect(Collectors.toList());
	}

	public Client findByMobileNumber(String mobileNumber) {

		return clientMap.entrySet().stream().filter(v -> mobileNumber.equals(v.getValue().getMobileNumber()))
				.map(Map.Entry::getValue).findFirst().orElse(null);

	}

	public Client findByIdNumber(String idNumber) {

		return clientMap.entrySet().stream().filter(v -> idNumber.equals(v.getValue().getIdNumber()))
				.map(Map.Entry::getValue).findFirst().orElse(null);

	}

	public Client persist(Client client) {
		clientMap.put(client.getIdNumber(), client);
		return client;
	}

	public List<Client> findByAll() {

		return clientMap.values().stream().collect(Collectors.toList());

	}
}
