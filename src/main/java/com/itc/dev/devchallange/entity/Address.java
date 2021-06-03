package com.itc.dev.devchallange.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

	private String city;
	private String country;
	private String streetLine1;
	private String streetLine2;
}
