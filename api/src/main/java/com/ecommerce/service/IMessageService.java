package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.ecommerce.model.Message;

public interface IMessageService {

	Iterable<Message> findAll(Sort sort);

	Optional<Message> findById(String id);

	Message save(Message message);

	void delete(String id);

	List<Message> findByToUserId(String toUserId, Sort sort);
}
