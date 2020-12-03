package com.ecommerce.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FileName {
	
	@NotBlank
	private String fileName;

	public FileName() {}
}
