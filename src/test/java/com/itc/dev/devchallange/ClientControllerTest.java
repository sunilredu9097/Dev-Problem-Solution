package com.itc.dev.devchallange;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
//import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itc.dev.devchallange.entity.Address;
import com.itc.dev.devchallange.entity.Client;
import com.itc.dev.devchallange.exception.DataAlreadyExistException;
import com.itc.dev.devchallange.exception.DataNotFoundException;
import com.itc.dev.devchallange.exception.IDNumberIsNotValidSAIdException;
import com.itc.dev.devchallange.service.ClientService;
import com.itc.dev.devchallange.serviceImpl.ClientServiceImpl;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevChallangeApplication.class)
@AutoConfigureMockMvc
public class ClientControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Spy
	private ClientService clientService;

	@Test
	public void should_create_valid_Client_and_return_created_status() throws Exception {
		Client input = createMock();
		input.setIdNumber("4001014800084");
		input.setMobileNumber("7865653544");
		Mockito.when(clientService.create(input)).thenReturn(input);
		mockMvc.perform(post("/client/api/v1/").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(input))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", Matchers.equalTo("JP")))
				.andExpect(jsonPath("$.idNumber", Matchers.equalTo("4001014800084")))
				.andExpect(jsonPath("$.mobileNumber", Matchers.equalTo("7865653544")));

	}

	@Test
	public void should_not_create_invalid_client_and_return_bad_request_status() throws Exception {
		Client input = createMock();
		input.setFirstName(null);
		mockMvc.perform(post("/client/api/v1/").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(input))).andExpect(status().isBadRequest());

	}

	@Test
	public void should_not_create_client_with_duplicate_mobile_number_and_return_conflict_status() throws Exception {
		Client input = createMock();
		input.setIdNumber("2303014800086");
		mockMvc.perform(post("/client/api/v1/").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(input))).andExpect(status().isConflict())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataAlreadyExistException))
				.andExpect(result -> assertEquals("Client already exist with Mobile Number: " + input.getMobileNumber(),
						result.getResolvedException().getMessage()));

	}

	@Test
	public void should_not_create_client_with_invalid_SA_Id_number_and_return_bad_request_status() throws Exception {
		Client input = createMock();
		input.setIdNumber("3803014800086");
		mockMvc.perform(post("/client/api/v1/").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapToJson(input)))
				.andExpect(status().isBadRequest())
				.andExpect(
						result -> assertTrue(result.getResolvedException() instanceof IDNumberIsNotValidSAIdException))
				.andExpect(result -> assertEquals("SA ID number is not valid: " + input.getIdNumber(),
						result.getResolvedException().getMessage()));

	}

	@Test
	public void should_get_valid_client_with_ok_status() throws Exception {
		mockMvc.perform(get("/client/api/v1/?mobileNumber=8754565356").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].firstName", Matchers.equalTo("JP")))
				.andExpect(jsonPath("$[0].idNumber", Matchers.equalTo("7801014800084")))
				.andExpect(jsonPath("$[0].mobileNumber", Matchers.equalTo("8754565356")));
	}

	@Test
	public void should_no_get_unknown_client_with_not_found_status() throws Exception {
		mockMvc.perform(get("/client/api/v1/?mobileNumber=8754565334").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException))
				.andExpect(result -> assertEquals("Client does not exist for given data",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void should_not_update_unknown_client_and_return_not_found_status() throws Exception {
		Client input = createMock();
		input.setIdNumber("3801014800084");
		input.setFirstName("steve");
		mockMvc.perform(put("/client/api/v1/" + input.getIdNumber()).contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(input))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException))
				.andExpect(result -> assertEquals("Client does not exist for given id Number: " + input.getIdNumber(),
						result.getResolvedException().getMessage()));
	}

	@Test
	public void should_update_valid_client_and_return_No_Content_status() throws Exception {
		Client input = createMock();
		input.setFirstName("steve");
		mockMvc.perform(put("/client/api/v1/" + input.getIdNumber()).contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(input))).andExpect(status().isNoContent())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", Matchers.equalTo("steve")))
				.andExpect(jsonPath("$.idNumber", Matchers.equalTo("7801014800084")))
				.andExpect(jsonPath("$.mobileNumber", Matchers.equalTo("8754565356")));
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	private Client createMock() {

		Client input = Client.builder().firstName("JP")
				.lastName("Dumini").mobileNumber("8754565356").idNumber("7801014800084").Address(Address.builder()
						.city("Durban").country("South Africa").streetLine1("Line 1").streetLine2("Line 2").build())
				.build();

		return input;
	}
}
