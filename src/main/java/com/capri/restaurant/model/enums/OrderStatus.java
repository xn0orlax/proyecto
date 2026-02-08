package com.capri.restaurant.model.enums;

public enum OrderStatus {
	IN_PROGRESS("En progreso"), COMPLETED("Entregado"), CANCELLED("Cancelado");

	private final String description;

	private OrderStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
