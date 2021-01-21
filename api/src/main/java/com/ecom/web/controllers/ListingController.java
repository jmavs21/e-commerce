package com.ecom.web.controllers;

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
import org.springframework.web.multipart.MultipartFile;

import com.ecom.domain.listings.FileName;
import com.ecom.domain.listings.Listing;
import com.ecom.domain.listings.ListingService;
import com.ecom.domain.listings.Location;
import com.ecom.domain.storage.FileType;
import com.ecom.domain.storage.StorageService;
import com.ecom.domain.users.User;
import com.ecom.domain.users.UserService;
import com.ecom.web.dto.Image;
import com.ecom.web.dto.ListingDto;
import com.ecom.web.dto.ListingDtoReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/listings")
public class ListingController {

  private final ListingService listingService;
  private final UserService userService;
  private final ModelMapper modelMapper;
  private final StorageService storageService;

  @Value("${assets.base.url}")
  private String assetsBaseUrl;

  public ListingController(
      ListingService listingService,
      UserService userService,
      ModelMapper modelMapper,
      StorageService storageService) {
    this.listingService = listingService;
    this.userService = userService;
    this.modelMapper = modelMapper;
    this.storageService = storageService;
  }

  @GetMapping
  public Collection<ListingDto> findFeed(Authentication authentication) {
    User user = getUser(authentication);
    return asListingDto(listingService.findByUserIdNot(user.getId(), Sort.by("title")));
  }

  @GetMapping("/mylistings")
  public Collection<ListingDto> findMyListings(Authentication authentication) {
    User user = getUser(authentication);
    return asListingDto(listingService.findByUserId(user.getId(), Sort.by("title")));
  }

  @GetMapping(value = "/{id}")
  public ListingDto findOne(@PathVariable String id) {
    return entityToDto(getListing(id));
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    Listing listing = getListing(id);
    listingService.delete(listing.getId());
    for (FileName fileName : listing.getImages()) {
      storageService.deleteResource(fileName.getFileName());
    }
  }

  @GetMapping("/assets/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getAsset(@PathVariable String filename) {
    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }

  @PostMapping(consumes = {"multipart/form-data"})
  @ResponseStatus(HttpStatus.CREATED)
  public ListingDto create(
      Authentication authentication, @ModelAttribute ListingDtoReq listingDtoReq) {
    User user = getUser(authentication);
    List<MultipartFile> images = listingDtoReq.getImages();
    for (MultipartFile multiImage : images) {
      storageService.store(multiImage);
    }
    Listing listing = listingService.save(dtoToEntity(listingDtoReq, user.getId()));
    return entityToDto(listing);
  }

  @PutMapping("/{id}")
  public ListingDto updateListing(
      @PathVariable("id") String id, @Valid @RequestBody ListingDto updatedListing) {
    Listing listing = getListing(id);
    listing.setTitle(updatedListing.getTitle());
    listing.setCategoryId(updatedListing.getCategoryId());
    listing.setUserId(updatedListing.getUserId());
    listing.setLocation(updatedListing.getLocation());
    listing.setPrice(updatedListing.getPrice());
    return entityToDto(listingService.save(listing));
  }

  private Collection<ListingDto> asListingDto(Iterable<Listing> listings) {
    List<ListingDto> listingsDtos = new ArrayList<>();
    for (Listing listing : listings) {
      listingsDtos.add(entityToDto(listing));
    }
    return listingsDtos;
  }

  private Listing getListing(String id) {
    return listingService
        .findById(id)
        .orElseThrow(
            () ->
                new DataRetrievalFailureException("The listing with the given Id was not found."));
  }

  private User getUser(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null)
      throw new DataRetrievalFailureException("User not found.");
    return (User) authentication.getPrincipal();
  }

  protected ListingDto entityToDto(Listing entity) {
    ListingDto dto = modelMapper.map(entity, ListingDto.class);
    String userName = getUserName(entity.getUserId());
    dto.setUserName(userName);
    List<Image> imagesDto = new ArrayList<>();
    List<FileName> images = entity.getImages();
    for (FileName fileName : images) {
      String name = fileName.getFileName();
      Image image = new Image();
      image.setUrl(assetsBaseUrl + name + FileType.FULL.extension());
      image.setThumbnailUrl(assetsBaseUrl + name + FileType.THUMB.extension());
      imagesDto.add(image);
    }
    dto.setImages(imagesDto);
    return dto;
  }

  private String getUserName(String id) {
    return userService
        .findById(id)
        .orElseThrow(
            () -> new DataRetrievalFailureException("The listing with the user Id was not found."))
        .getName();
  }

  protected Listing dtoToEntity(ListingDtoReq dto, String userId) {
    Listing listing = modelMapper.map(dto, Listing.class);
    listing.setUserId(userId);
    List<FileName> images = new ArrayList<>();
    List<MultipartFile> multiImages = dto.getImages();
    for (MultipartFile multipartFile : multiImages) {
      FileName fileName = new FileName();
      fileName.setFileName(multipartFile.getOriginalFilename());
      images.add(fileName);
    }
    listing.setImages(images);
    try {
      listing.setLocation(
          modelMapper.map(new ObjectMapper().readTree(dto.getLocation()), Location.class));
    } catch (IOException e) {
      throw new DataRetrievalFailureException("The listing with given location was invalid.");
    }
    return listing;
  }
}
