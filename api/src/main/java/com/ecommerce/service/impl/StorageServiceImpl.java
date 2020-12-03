package com.ecommerce.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.service.IStorageService;
import com.ecommerce.web.error.StorageException;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class StorageServiceImpl implements IStorageService {

	private final Path rootLocation;

	public StorageServiceImpl(@Value("${upload.assets.location}") String uploadAssetsLocation) {
		rootLocation = Paths.get(uploadAssetsLocation);
		init();
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty())
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			Thumbnails.of(file.getInputStream()).size(2000, 2000).outputQuality(0.5).outputFormat("jpg")
					.toFile(rootLocation.resolve(file.getOriginalFilename()).toString() + "_full");
			Thumbnails.of(file.getInputStream()).size(100, 100).outputQuality(0.3).outputFormat("jpg")
					.toFile(rootLocation.resolve(file.getOriginalFilename()).toString() + "_thumb");
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable())
				return resource;
			else
				throw new StorageException("Could not read file: " + filename);
		} catch (MalformedURLException e) {
			throw new StorageException("Could not read file: " + filename, e);
		}
	}
	
	@Override
  public void deleteResource(String filename) {
		try {
      Files.delete(rootLocation.resolve(filename + "_full.jpg"));
      Files.delete(rootLocation.resolve(filename + "_thumb.jpg"));
    } catch (IOException e) {
    	throw new StorageException("Could not delete file", e);
    }
  }

	@Override
	public void init() {
		try {
			if (Files.exists(rootLocation))
				return;
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
