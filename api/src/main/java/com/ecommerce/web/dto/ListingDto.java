package com.ecommerce.web.dto;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.ecommerce.model.Image;
import com.ecommerce.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListingDto {

	@JsonProperty("id")
	private String id;

	@NotBlank
	private String title;
	
	@NotBlank
	private String description;

	private List<Image> images;

	@Min(value = 0)
	@Max(value = 1_000_000)
	private Float price;

	@NotBlank
	private String categoryId;

	private String userId;
	
	private String userName;

	private Location location;

	public ListingDto() {
	}
}
