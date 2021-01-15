package com.ecom.domain.messages;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

public interface MessageService {

  Iterable<Message> findAll(Sort sort);

  Optional<Message> findById(String id);

  Message save(Message message);

  void delete(String id);

  List<Message> findByToUserId(String toUserId, Sort sort);
}
