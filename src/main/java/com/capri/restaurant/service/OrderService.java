package com.capri.restaurant.service;

import java.util.List;
import java.util.Optional;

import com.capri.restaurant.dto.OrderFormDto;
import com.capri.restaurant.model.Order;

public interface OrderService {
	
	List<Order> listAll();

	Order saveOrderWithItems(OrderFormDto orderFormDto);

	Optional<Order> getById(Long id);

	void delete(Long id);
	
	Order save(Order order);
	
	List<Order> findAllByCurrentBusinessDay();
	
}
