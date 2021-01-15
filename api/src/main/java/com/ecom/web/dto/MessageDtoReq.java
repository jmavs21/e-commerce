package com.ecom.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageDtoReq {

  @NotBlank private String listingId;

  @NotBlank private String message;

  public MessageDtoReq() {}
}
