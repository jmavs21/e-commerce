package com.ecom.domain.messages;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

interface MessageRepo extends MongoRepository<Message, String> {

  List<Message> findByToUserId(String toUserId, Sort sort);
}
