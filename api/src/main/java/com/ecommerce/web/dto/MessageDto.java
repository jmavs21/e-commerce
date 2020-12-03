package com.ecommerce.web.dto;

import com.ecommerce.model.FromToUser;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageDto {

	@JsonProperty("id")
	private String id;

	private String listingId;

	private Long dateTime;

	private String content;

	private FromToUser fromUser;

	private FromToUser toUser;

	public MessageDto() {
	}
}
