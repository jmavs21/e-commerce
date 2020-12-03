package com.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Message;
import com.ecommerce.repo.IMessageRepo;
import com.ecommerce.service.IMessageService;

@Service
public class MessageServiceImpl implements IMessageService {

	private IMessageRepo messageRepo;

	public MessageServiceImpl(IMessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	@Override
	public Iterable<Message> findAll(Sort sort) {
		return messageRepo.findAll(sort);
	}

	@Override
	public Optional<Message> findById(String id) {
		return messageRepo.findById(id);
	}

	@Override
	public Message save(Message message) {
		return messageRepo.save(message);
	}

	@Override
	public void delete(String id) {
		messageRepo.deleteById(id);
	}

	@Override
	public List<Message> findByToUserId(String toUserId, Sort sort) {
		return messageRepo.findByToUserId(toUserId, sort);
	}
}