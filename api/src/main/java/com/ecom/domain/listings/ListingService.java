package com.ecom.domain.listings;

import java.util.Optional;

import org.springframework.data.domain.Sort;

public interface ListingService {

  Iterable<Listing> findAll(Sort sort);

  Iterable<Listing> findByUserId(String userId, Sort sort);

  Iterable<Listing> findByUserIdNot(String userId, Sort sort);

  Optional<Listing> findById(String id);

  Listing save(Listing listing);

  void delete(String id);
}
