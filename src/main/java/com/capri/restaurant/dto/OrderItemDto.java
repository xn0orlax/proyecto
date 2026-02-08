package com.capri.restaurant.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.capri.restaurant.model.enums.OrderStatus;

public class OrderItemDto {
	
	private Long id;
	private LocalDateTime createdAt;
	private LocalDateTime closeAt;
	private OrderStatus status;
	private BigDecimal total;
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getCloseAt() {
		return closeAt;
	}

	public void setCloseAt(LocalDateTime closeAt) {
		this.closeAt = closeAt;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "OrderByDayDto [id=" + id + ", createdAt=" + createdAt + ", closeAt=" + closeAt + ", status="
				+ status + ", total=" + total + ", description=" + description + "]";
	}
	
}
