package com.ecom.domain.messages;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
class MessageServiceImpl implements MessageService {

  private MessageRepo messageRepo;

  public MessageServiceImpl(MessageRepo messageRepo) {
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
