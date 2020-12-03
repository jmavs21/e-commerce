package com.ecommerce.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.FileName;
import com.ecommerce.model.Image;
import com.ecommerce.model.Listing;
import com.ecommerce.model.Location;
import com.ecommerce.model.User;
import com.ecommerce.service.IListingService;
import com.ecommerce.service.IStorageService;
import com.ecommerce.service.IUserService;
import com.ecommerce.web.dto.ListingDto;
import com.ecommerce.web.dto.ListingDtoReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/listings")
public class ListingController {

	private final IListingService listingService;
	private final IUserService userService;
	private final ModelMapper modelMapper;
	private final IStorageService storageService;

	@Value("${assets.base.url}")
	private String assetsBaseUrl;

	public ListingController(IListingService listingService, IUserService userService, ModelMapper modelMapper,
			IStorageService storageService) {
		this.listingService = listingService;
		this.userService = userService;
		this.modelMapper = modelMapper;
		this.storageService = storageService;
	}

	@GetMapping
	public Collection<ListingDto> findFeed(Authentication authentication) {
		User user = getUser(authentication);
		Iterable<Listing> listings = listingService.findByUserIdNot(user.getId(), Sort.by("title"));
		List<ListingDto> listingsDtos = new ArrayList<>();
		listings.forEach(l -> listingsDtos.add(entityToDto(l)));
		return listingsDtos;
	}

	@GetMapping("/mylistings")
	public Collection<ListingDto> findMyListings(Authentication authentication) {
		User user = getUser(authentication);
		Iterable<Listing> listings = listingService.findByUserId(user.getId(), Sort.by("title"));
		List<ListingDto> listingsDtos = new ArrayList<>();
		listings.forEach(l -> listingsDtos.add(entityToDto(l)));
		return listingsDtos;
	}

	@GetMapping(value = "/{id}")
	public ListingDto findOne(@PathVariable String id) {
		Listing listing = listingService.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("The listing with the given Id was not found."));
		return entityToDto(listing);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) {
		Listing listing = listingService.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("The listing with the given Id was not found."));
		listingService.delete(listing.getId());
		listing.getImages().stream().forEach(i -> storageService.deleteResource(i.getFileName()));
	}

	@GetMapping("/assets/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getAsset(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping(consumes = { "multipart/form-data" })
	@ResponseStatus(HttpStatus.CREATED)
	public ListingDto create(Authentication authentication, @ModelAttribute ListingDtoReq listingDtoReq) {
		User user = getUser(authentication);
		listingDtoReq.getImages().stream().forEach(i -> {
			storageService.store(i);
		});
		Listing listing = listingService.save(dtoToEntity(listingDtoReq, user.getId()));
		return entityToDto(listing);
	}

	@PutMapping("/{id}")
	public ListingDto updateListing(@PathVariable("id") String id, @Valid @RequestBody ListingDto updatedListing) {
		Listing listing = listingService.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("The listing with the given Id was not found."));
		listing.setTitle(updatedListing.getTitle());
		listing.setCategoryId(updatedListing.getCategoryId());
		listing.setUserId(updatedListing.getUserId());
		listing.setLocation(updatedListing.getLocation());
		listing.setPrice(updatedListing.getPrice());
		return entityToDto(listingService.save(listing));
	}

	private User getUser(Authentication authentication) {
		if (authentication == null || authentication.getPrincipal() == null)
			throw new DataRetrievalFailureException("User not found.");
		return (User) authentication.getPrincipal();
	}

	protected ListingDto entityToDto(Listing entity) {
		ListingDto dto = modelMapper.map(entity, ListingDto.class);
		String userName = userService.findById(entity.getUserId())
				.orElseThrow(() -> new DataRetrievalFailureException("The listing with the user Id was not found."))
				.getName();
		dto.setUserName(userName);
		List<Image> imagesDto = new ArrayList<>();
		entity.getImages().stream().forEach(i -> {
			String name = i.getFileName();
			Image image = new Image();
			image.setUrl(assetsBaseUrl + name + "_full.jpg");
			image.setThumbnailUrl(assetsBaseUrl + name + "_thumb.jpg");
			imagesDto.add(image);
		});
		dto.setImages(imagesDto);
		return dto;
	}

	protected Listing dtoToEntity(ListingDtoReq dto, String userId) {
		Listing listing = modelMapper.map(dto, Listing.class);
		listing.setUserId(userId);
		List<FileName> images = new ArrayList<>();
		dto.getImages().stream().forEach(i -> {
			FileName fileName = new FileName();
			fileName.setFileName(i.getOriginalFilename());
			images.add(fileName);
		});
		listing.setImages(images);
		try {
			listing.setLocation(modelMapper.map(new ObjectMapper().readTree(dto.getLocation()), Location.class));
		} catch (IOException e) {
			throw new DataRetrievalFailureException("The listing with given location was invalid.");
		}
		return listing;
	}
}
