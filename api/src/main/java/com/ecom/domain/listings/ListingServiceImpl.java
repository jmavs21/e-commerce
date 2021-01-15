package com.ecom.domain.listings;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
class ListingServiceImpl implements ListingService {

  private ListingRepo listingRepo;

  public ListingServiceImpl(ListingRepo listingRepo) {
    this.listingRepo = listingRepo;
  }

  @Override
  public Iterable<Listing> findAll(Sort sort) {
    return listingRepo.findAll(sort);
  }

  @Override
  public Iterable<Listing> findByUserId(String userId, Sort sort) {
    return listingRepo.findByUserId(userId, sort);
  }

  @Override
  public Iterable<Listing> findByUserIdNot(String userId, Sort sort) {
    return listingRepo.findByUserIdNot(userId, sort);
  }

  @Override
  public Optional<Listing> findById(String id) {
    return listingRepo.findById(id);
  }

  @Override
  public Listing save(Listing listing) {
    return listingRepo.save(listing);
  }

  @Override
  public void delete(String id) {
    listingRepo.deleteById(id);
  }
}
