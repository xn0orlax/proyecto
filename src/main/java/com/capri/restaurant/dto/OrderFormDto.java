package com.capri.restaurant.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.capri.restaurant.model.enums.OrderStatus;

public class OrderFormDto {

	private Long orderId;

	private String orderDescription;

	private LocalDateTime orderUpdatedAt;

	private List<OrderItemFormDto> items = new ArrayList<>();

	private BigDecimal total;

	private OrderStatus status;

	private Boolean updateItems;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public LocalDateTime getOrderUpdatedAt() {
		return orderUpdatedAt;
	}

	public void setOrderUpdatedAt(LocalDateTime orderUpdatedAt) {
		this.orderUpdatedAt = orderUpdatedAt;
	}

	public List<OrderItemFormDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemFormDto> items) {
		this.items = items;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Boolean getUpdateItems() {
		return updateItems;
	}

	public void setUpdateItems(Boolean updateItems) {
		this.updateItems = updateItems;
	}

	@Override
	public String toString() {
		return "OrderFormDto [orderId=" + orderId + ", orderDescription=" + orderDescription + ", orderUpdatedAt="
				+ orderUpdatedAt + ", items=" + items + ", status=" + status + ", updateItems=" + updateItems + "]";
	}

}
