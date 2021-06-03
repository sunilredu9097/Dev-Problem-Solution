package com.itc.dev.devchallange.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itc.dev.devchallange.entity.Client;
import com.itc.dev.devchallange.exception.DataAlreadyExistException;
import com.itc.dev.devchallange.exception.DataNotFoundException;
import com.itc.dev.devchallange.exception.IDNumberIsNotValidSAIdException;
import com.itc.dev.devchallange.helper.IDValidatorGeneric;
import com.itc.dev.devchallange.repository.ClientRepository;
import com.itc.dev.devchallange.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	private ClientRepository repository;

	@Override
	public List<Client> fetch(String firstName, String mobileNumber, String idNumber) {
		String search = filterSearchParam(firstName, mobileNumber, idNumber);
		List<Client> clients = null;
		if (search != null)
			clients = repository.findByNameOrMobileNumberOIdnumber(search);
		else
			clients = repository.findByAll();

		if (clients.size() < 1)
			throw new DataNotFoundException("Client does not exist for given data");
		return clients;

		/*
		 * if (firstName != null && !firstName.isEmpty()) { return
		 * repository.findByFirstName(firstName.trim()); } else { if (idNumber != null
		 * && !idNumber.trim().isEmpty()) { Boolean isIdNumberValid = new
		 * IDValidatorGeneric().isValid(idNumber.trim()); if (isIdNumberValid) {
		 * Optional.ofNullable(repository.findByIdNumber(idNumber.trim())).ifPresent(
		 * client -> { clients.add(client); }); } else throw new
		 * IDNumberIsNotValidSAIdException("Please provide valid SA ID Number"); } else
		 * { if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
		 * Optional.ofNullable(repository.findByIdNumber(idNumber.trim())).ifPresent(
		 * client -> { clients.add(client); }); }else { return repository.findByAll(); }
		 * }
		 * 
		 * }
		 */
		// return clients;
	}

	@Override
	public Client create(Client client) {
		Boolean isIdNumberValid = new IDValidatorGeneric().isValid(client.getIdNumber().trim());
		if (isIdNumberValid) {
			final String idNumber = client.getIdNumber();
			Optional.ofNullable(repository.findByIdNumber(client.getIdNumber().trim())).ifPresent(value -> {
				throw new DataAlreadyExistException("Client already exist with Id Number: " + idNumber);
			});

			Optional.ofNullable(client.getMobileNumber()).ifPresent(mobileNumber -> {
				if (Optional.of(isMobileNumberExist(mobileNumber)).orElse(true))
					throw new DataAlreadyExistException("Client already exist with Mobile Number: " + mobileNumber);
			});
			client = repository.persist(client);
		} else {
			throw new IDNumberIsNotValidSAIdException("SA ID number is not valid: " + client.getIdNumber());
		}
		return client;
	}

	@Override
	public Client update(String idNumber, Client client) {

		Client clientFromDb = Optional.ofNullable(repository.findByIdNumber(idNumber.trim()))
				.orElseThrow(() -> new DataNotFoundException("Client does not exist for given id Number: " + idNumber));

		if (!isMobileNumberExist(client.getMobileNumber())) {
			clientFromDb.setMobileNumber(client.getMobileNumber());
		}
		clientFromDb.setFirstName(client.getFirstName());
		clientFromDb.setLastName(client.getLastName());
		clientFromDb.setAddress(client.getAddress());

		client = repository.persist(clientFromDb);
		return client;
	}

	private Boolean isMobileNumberExist(String mobileNumber) {
		return Optional.ofNullable(repository.findByMobileNumber(mobileNumber.trim())).isPresent();
	}

	private String filterSearchParam(String firstName, String mobileNumber, String idNumber) {
		if ((firstName != null && !firstName.isEmpty())) {
			return firstName;
		} else {
			if (idNumber != null && !idNumber.trim().isEmpty()) {
				Boolean isIdNumberValid = new IDValidatorGeneric().isValid(idNumber.trim());
				if(!isIdNumberValid)
				  throw new IDNumberIsNotValidSAIdException("Please provide valid SA ID Number");
				return idNumber;
			} else {
				if (mobileNumber != null && !mobileNumber.trim().isEmpty())
					return mobileNumber;
				else
					return null;
			}

		}

	}
}