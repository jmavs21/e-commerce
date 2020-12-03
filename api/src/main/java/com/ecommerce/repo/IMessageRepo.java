package com.ecommerce.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.model.Message;

public interface IMessageRepo extends MongoRepository<Message, String> {

	List<Message> findByToUserId(String toUserId, Sort sort);
}
