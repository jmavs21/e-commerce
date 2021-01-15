package com.ecom.domain.messages;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(value = "messages")
@Data
public class Message {

  @Id private String id;

  private String listingId;

  private Long dateTime;

  private String content;

  private String fromUserId;

  private String toUserId;

  public Message() {}
}
