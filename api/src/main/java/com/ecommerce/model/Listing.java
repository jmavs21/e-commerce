package com.ecommerce.model;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(value = "listings")
@Data
public class Listing {

	@Id
	private String id;

	@NotBlank
	private String title;
	
	private String description;

	private List<FileName> images;

	@Min(value = 0)
	@Max(value = 1_000_000)
	private Float price;

	@NotBlank
	private String categoryId;

	@NotBlank
	private String userId;

	private Location location;

	public Listing() {
	}
}
