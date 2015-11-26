package org.thisarattr.domain.registrar.exception;

public class DomainRegistrarBusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

	public DomainRegistrarBusinessException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
