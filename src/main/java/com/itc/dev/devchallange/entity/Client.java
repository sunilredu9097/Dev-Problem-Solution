package com.itc.dev.devchallange.entity;

import javax.validation.constraints.Pattern;

import com.itc.dev.devchallange.exception.NotEmpty;
import com.itc.dev.devchallange.exception.NotNull;
import com.itc.dev.devchallange.helper.ValidIdNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Client {
	@NotNull(fieldName="First Name")
	@NotEmpty(fieldName="First Name")
	private String firstName;
	@NotEmpty(fieldName="Last Name")
	private String lastName;
	@NotEmpty(fieldName="Mobile Number")
	@Pattern(regexp = "(0/91)?[7-9][0-9]{9}",message="Mobile Number must have 10 digits")
	private String mobileNumber;
	@NotEmpty(fieldName="ID Number")
	//@ValidIdNumber
	private String idNumber;
	private Address Address;
}
