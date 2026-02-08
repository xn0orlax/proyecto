package com.capri.restaurant.exception;

public class EmptyOrderException extends RuntimeException {

	private static final long serialVersionUID = -3682269174300751570L;
	
	public EmptyOrderException(String message) {
		super(message);
	}

}
