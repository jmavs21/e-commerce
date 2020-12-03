package com.ecommerce.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.model.Listing;

public interface IListingRepo extends MongoRepository<Listing, String>{

	Iterable<Listing> findByUserId(String userId, Sort sort);
	
	Iterable<Listing> findByUserIdNot(String userId, Sort sort);
}
