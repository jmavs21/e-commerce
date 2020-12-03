package com.ecommerce.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListingDtoReq {

	@NotBlank
	private String title;

	private String description;

	private List<MultipartFile> images;

	@NotBlank
	private String price;

	@NotBlank
	private String categoryId;

	private String location;

	public ListingDtoReq() {
	}
}
