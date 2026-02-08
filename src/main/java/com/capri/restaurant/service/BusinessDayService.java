package com.capri.restaurant.service;

import java.util.List;
import java.util.Optional;

import com.capri.restaurant.model.BusinessDay;
import com.capri.restaurant.model.enums.BusinessDayStatus;

public interface BusinessDayService {
	
	BusinessDay openDay();
	
	BusinessDay closeDay();
	
	Optional<BusinessDay> findByStatus(BusinessDayStatus status);
	
	List<BusinessDay> findAllByOrderByOpenedAtDesc();

	BusinessDay getOpenDay();
	
	BusinessDay findById(Long id);
	
}
