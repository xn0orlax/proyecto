package com.capri.restaurant.model.enums;

public enum BusinessDayStatus {
	
	OPEN("ABIERTO"), CLOSE("CERRADO");

	private final String description;

	private BusinessDayStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
