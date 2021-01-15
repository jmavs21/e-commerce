package com.ecom.domain.listings;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

interface ListingRepo extends MongoRepository<Listing, String> {

  Iterable<Listing> findByUserId(String userId, Sort sort);

  Iterable<Listing> findByUserIdNot(String userId, Sort sort);
}
