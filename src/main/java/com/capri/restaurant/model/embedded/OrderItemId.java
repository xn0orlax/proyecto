package com.capri.restaurant.model.embedded;

import java.util.Objects;

public class OrderItemId {

	private Long orderId;
	private Long productId;

	@Override
	public int hashCode() {
		return Objects.hash(orderId, productId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItemId other = (OrderItemId) obj;
		return Objects.equals(orderId, other.orderId) && Objects.equals(productId, other.productId);
	}

}
