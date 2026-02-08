package com.capri.restaurant.dto;

import java.math.BigDecimal;

import com.capri.restaurant.model.Size;

public class OrderItemFormDto {

	private int index;
	private Long itemId;
	private String itemName;
	private BigDecimal itemPrice;
	private Size itemSize;
	private int quantity;
	private BigDecimal total;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Size getItemSize() {
		return itemSize;
	}

	public void setItemSize(Size itemSize) {
		this.itemSize = itemSize;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "OrderItemFormDto [index=" + index + ", itemId=" + itemId + ", itemName=" + itemName + ", itemPrice="
				+ itemPrice + ", itemSize=" + itemSize + ", quantity=" + quantity + ", total=" + total + "]";
	}

}
