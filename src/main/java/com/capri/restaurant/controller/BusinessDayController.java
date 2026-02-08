package com.capri.restaurant.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.capri.restaurant.dto.BusinessDayDto;
import com.capri.restaurant.dto.OrderItemDto;
import com.capri.restaurant.model.BusinessDay;
import com.capri.restaurant.model.Order;
import com.capri.restaurant.model.enums.BusinessDayStatus;
import com.capri.restaurant.model.enums.OrderStatus;
import com.capri.restaurant.service.BusinessDayService;

@Controller
@RequestMapping("/businessday")
public class BusinessDayController {
	
	@Autowired
	private BusinessDayService businessDayService;
	
	@GetMapping
	public String list(Model model) {
		List<BusinessDay> businessDays = businessDayService.findAllByOrderByOpenedAtDesc();
		BusinessDay currentDay = businessDayService.findByStatus(BusinessDayStatus.OPEN).orElse(null);
		model.addAttribute("active", "days");
		model.addAttribute("currentDay", currentDay);
		model.addAttribute("days", businessDays);
		return "business-day/business-day";
	}
	
	@GetMapping("/open")
	public String open(Model model, RedirectAttributes ra) {
		try {
			businessDayService.openDay();
			ra.addFlashAttribute("success", true);
			ra.addFlashAttribute("msgType", "msg-open-success");
		} catch (Exception e) {
			ra.addFlashAttribute("success", false);
			ra.addFlashAttribute("msgType", "msg-open-error");
		}
		return "redirect:/businessday";
	}
	
	@GetMapping("/close/{id}")
	public String close(RedirectAttributes ra) {
		BusinessDay businessDay = businessDayService.closeDay();
		if (businessDay.getStatus() == BusinessDayStatus.OPEN) {
			ra.addFlashAttribute("success", false);
			ra.addFlashAttribute("msgType", "msg-close-error");
			return "redirect:/businessday";
		}
		ra.addFlashAttribute("success", true);
		ra.addFlashAttribute("msgType", "msg-close-success");
		return "redirect:/businessday";
	}
	
	@GetMapping("/details/{id}")
	public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
		try {
			BusinessDay businessDay = businessDayService.findById(id);
			BusinessDayDto dto = new BusinessDayDto();
			dto.setOpenAt(businessDay.getOpenedAt());
			dto.setCloseAt(businessDay.getClosedAt());
			dto.setTotal(businessDay.getTotal());
			if (dto.getTotal().compareTo(BigDecimal.ZERO) == 0) {
				BigDecimal total = BigDecimal.ZERO;
				for (Order order : businessDay.getOrders()) {
					if (order.getStatus() != OrderStatus.CANCELLED) {
						total = total.add(order.getTotal());
					}
				}
				dto.setTotal(total);
			}
			List<OrderItemDto> orders = new ArrayList<>();
			businessDay.getOrders().stream().forEach(order -> {
				OrderItemDto itemDto = new OrderItemDto();
				itemDto.setId(order.getId());
				itemDto.setCreatedAt(order.getCreatedAt());
				itemDto.setCloseAt(order.getUpdatedAt());
				itemDto.setStatus(order.getStatus());
				itemDto.setTotal(order.getTotal());
				itemDto.setDescription(order.getDescription());
				orders.add(itemDto);
			});
			dto.setOrders(orders);
			model.addAttribute("dayDto", dto);
		} catch (IllegalStateException e) {
			ra.addFlashAttribute("success", false);
			ra.addFlashAttribute("messageDescription", e.getMessage());
			return "redirect:/businessday";
		}
		return "business-day/business-day-detail";
	}
	
}
