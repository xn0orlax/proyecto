package com.capri.restaurant.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capri.restaurant.dto.OrderFormDto;
import com.capri.restaurant.dto.OrderItemFormDto;
import com.capri.restaurant.exception.EmptyOrderException;
import com.capri.restaurant.model.BusinessDay;
import com.capri.restaurant.model.Order;
import com.capri.restaurant.model.OrderItem;
import com.capri.restaurant.model.Product;
import com.capri.restaurant.model.enums.BusinessDayStatus;
import com.capri.restaurant.model.enums.OrderStatus;
import com.capri.restaurant.repository.OrderRepository;
import com.capri.restaurant.repository.ProductRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BusinessDayService businessDayService;

	@Override
	public List<Order> listAll() {
		return orderRepository.findAll();
	}

	@Transactional
	@Override
	public Order saveOrderWithItems(OrderFormDto orderFormDto) {
		BusinessDay businessDay = businessDayService.getOpenDay();
		if (businessDay == null || businessDay.getStatus() != BusinessDayStatus.OPEN) {
			throw new IllegalStateException("Business day is closed");
		}
		boolean isNew = orderFormDto.getOrderId() == null;
		Order order;
		if (isNew) {
			order = new Order();
			order.setCreatedAt(LocalDateTime.now());
			order.setStatus(OrderStatus.IN_PROGRESS);
			order.setBusinessDay(businessDay);
		} else {
			order = orderRepository.findById(orderFormDto.getOrderId())
					.orElseThrow(() -> new IllegalArgumentException("Order not found"));
		}
		order.setDescription(orderFormDto.getOrderDescription());
		order.setUpdatedAt(LocalDateTime.now());
		if (Boolean.TRUE.equals(orderFormDto.getUpdateItems())) {
			Map<Long, OrderItem> existingItems = order.getItems().stream()
					.collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));
			orderFormDto.getItems().removeIf(item -> item.getItemId() == null || item.getQuantity() <= 0);
			if (orderFormDto.getItems().size() == 0) {
				if (isNew) {
					throw new EmptyOrderException("No se puede generar una orden vacía");
				} else {
					throw new EmptyOrderException("No se puede actualizar una orden vacía");
				}
			}
			order.getItems().clear();
			order.setShippingCost(orderFormDto.getShippingCost());
			order.setTotal(BigDecimal.ZERO);
			for (OrderItemFormDto dto : orderFormDto.getItems()) {
				Product product = productRepository.findById(dto.getItemId())
						.orElseThrow(() -> new IllegalArgumentException("Product not found"));
				OrderItem item = existingItems.getOrDefault(dto.getItemId(), new OrderItem());
				item.setOrder(order);
				item.setProduct(product);
				item.setQuantity(dto.getQuantity());
				BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));
				item.setTotal(itemTotal);
				order.setTotal(order.getTotal().add(itemTotal));
				order.getItems().add(item);
			}
			order.setTotal(order.getTotal().add(order.getShippingCost()));
		}
		return orderRepository.save(order);
	}

	@Override
	public Optional<Order> getById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		orderRepository.deleteById(id);
	}

	@Override
	public Order save(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public List<Order> findAllByCurrentBusinessDay() {
		BusinessDay openDay = businessDayService.getOpenDay();
		return openDay != null ? openDay.getOrders() : List.of();
	}

}
