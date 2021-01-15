package com.ecom.error;

import org.springframework.web.client.RestClientException;

public class BadRequestException extends RestClientException {

	private static final long serialVersionUID = 2724742819671746423L;

	public BadRequestException(String msg) {
		super(msg);
	}

	public BadRequestException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
