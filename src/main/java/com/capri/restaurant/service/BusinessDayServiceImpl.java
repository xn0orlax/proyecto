package com.capri.restaurant.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capri.restaurant.model.BusinessDay;
import com.capri.restaurant.model.Order;
import com.capri.restaurant.model.enums.BusinessDayStatus;
import com.capri.restaurant.model.enums.OrderStatus;
import com.capri.restaurant.repository.BusinessDayRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BusinessDayServiceImpl implements BusinessDayService {
	
	@Autowired
	private BusinessDayRepository businessDayRepository;
	
	@Override
	public BusinessDay openDay() {
		boolean existsOpenDay = businessDayRepository.existsByStatus(BusinessDayStatus.OPEN);
		if (existsOpenDay) {
			throw new IllegalStateException("No se puede iniciar el día. Ya existe un día con estatus abierto");
		}
		BusinessDay day = new BusinessDay();
		day.setOpenedAt(LocalDateTime.now());
		day.setStatus(BusinessDayStatus.OPEN);
		day.setTotal(BigDecimal.ZERO);
		return businessDayRepository.save(day);
	}
	
	@Override
	public BusinessDay closeDay() {
		BusinessDay businessDay = businessDayRepository.findByStatus(BusinessDayStatus.OPEN).orElseThrow(() 
				-> new IllegalStateException("No se puede terminar el día. No existe algún día con estatus abierto"));
		List<Order> pendingOrders = businessDay.getOrders().stream().filter(order -> order.getStatus() == OrderStatus.IN_PROGRESS).collect(Collectors.toList());
		if (!pendingOrders.isEmpty()) {
			return businessDay;
		}
		BigDecimal total = BigDecimal.ZERO;
		for (Order order : businessDay.getOrders()) {
			if (order.getStatus() == OrderStatus.COMPLETED) {
				total = total.add(order.getTotal());
			}
		}
		businessDay.setTotal(total);
		businessDay.setClosedAt(LocalDateTime.now());
		businessDay.setStatus(BusinessDayStatus.CLOSE);
		return businessDayRepository.save(businessDay);
	}

	@Override
	public Optional<BusinessDay> findByStatus(BusinessDayStatus status) {
		return businessDayRepository.findByStatus(status);
	}

	@Override
	public List<BusinessDay> findAllByOrderByOpenedAtDesc() {
		return businessDayRepository.findAllByOrderByOpenedAtDesc();
	}
	
	@Override
	public BusinessDay getOpenDay() {
		return findByStatus(BusinessDayStatus.OPEN).orElse(null);
	}

	@Override
	public BusinessDay findById(Long id) {
		return businessDayRepository.findById(id).orElseThrow(() -> new IllegalStateException("No se puede localizar el día"));
	}
	
}
