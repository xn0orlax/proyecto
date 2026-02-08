package com.capri.restaurant.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductForm {

	private Long productId;
	
	@NotBlank(message = "El nombre es requerido")
	@Size(max = 100)
	private String productName;
	
	private String productDescription;
	
	@NotNull(message = "El precio es requerido")
	@DecimalMin(value = "0.01", message = "El precio debe de ser mayor a cero")
	private BigDecimal productPrice;
	
	private boolean productStatus;
	
	@NotNull(message = "El tamaño es requerido")
	private Long sizeId;

	@NotNull(message = "La categoría es requerida")
	private Long categoryId;

	public ProductForm() {
		this.productStatus = true;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public boolean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(boolean productStatus) {
		this.productStatus = productStatus;
	}

	public Long getSizeId() {
		return sizeId;
	}

	public void setSizeId(Long sizeId) {
		this.sizeId = sizeId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "ProductForm [productId=" + productId + ", productName=" + productName + ", productDescription="
				+ productDescription + ", productPrice=" + productPrice + ", productStatus=" + productStatus
				+ ", sizeId=" + sizeId + ", categoryId=" + categoryId + "]";
	}

}
