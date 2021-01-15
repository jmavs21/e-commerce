package com.ecom.error;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 1981394052897730667L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
