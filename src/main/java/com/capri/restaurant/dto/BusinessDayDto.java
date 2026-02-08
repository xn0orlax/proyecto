package com.capri.restaurant.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BusinessDayDto {

	private LocalDateTime openAt;
	private LocalDateTime closeAt;
	private BigDecimal total;
	private List<OrderItemDto> orders = new ArrayList<>();

	public LocalDateTime getOpenAt() {
		return openAt;
	}

	public void setOpenAt(LocalDateTime openAt) {
		this.openAt = openAt;
	}

	public LocalDateTime getCloseAt() {
		return closeAt;
	}

	public void setCloseAt(LocalDateTime closeAt) {
		this.closeAt = closeAt;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<OrderItemDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderItemDto> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "BusinessDayDto [openAt=" + openAt + ", closeAt=" + closeAt + ", total=" + total + ", orders=" + orders
				+ "]";
	}

}
