package com.capri.restaurant.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.capri.restaurant.dto.OrderFormDto;
import com.capri.restaurant.dto.OrderItemFormDto;
import com.capri.restaurant.exception.EmptyOrderException;
import com.capri.restaurant.model.BusinessDay;
import com.capri.restaurant.model.Category;
import com.capri.restaurant.model.Order;
import com.capri.restaurant.model.OrderItem;
import com.capri.restaurant.model.Product;
import com.capri.restaurant.model.enums.BusinessDayStatus;
import com.capri.restaurant.model.enums.OrderStatus;
import com.capri.restaurant.service.BusinessDayService;
import com.capri.restaurant.service.OrderService;
import com.capri.restaurant.service.ProductService;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private BusinessDayService businessDayService;

	private static final String ACTIVE = "orders";
	private static final String TITLE = "Órdenes";

	@GetMapping
	public String listCurrentOrders(Model model, RedirectAttributes ra) {
		BusinessDay businessDay = businessDayService.getOpenDay();
		BigDecimal total = BigDecimal.ZERO;
		if (businessDay != null) {
			businessDay.getOrders().sort(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
			for (Order order : businessDay.getOrders()) {
				total = total.add(order.getTotal());
			}
			model.addAttribute("orders", businessDay.getOrders());
		} else {
			model.addAttribute("orders", new ArrayList<>());
		}
		model.addAttribute("total", total);
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		return "orders/orders";
	}

	@GetMapping("/new")
	public String newOrderForm(Model model) {
		OrderFormDto orderForm = new OrderFormDto();
		List<Product> products = productService.findByStatusTrueOrderByCategoryNameAscNameAsc();
		Map<Category, List<OrderItemFormDto>> itemsByCategory = new LinkedHashMap<>();
		int count = 0;
		for (Product p : products) {
			itemsByCategory.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(toOrderItemDto(p, count));
			count++;
		}
		List<OrderItemFormDto> flatItems = new ArrayList<>();
		itemsByCategory.forEach((category, items) -> {
			items.forEach(item -> {
				OrderItemFormDto f = new OrderItemFormDto();
				f.setIndex(flatItems.size());
				f.setItemId(item.getItemId());
				f.setQuantity(0);
				f.setTotal(BigDecimal.ZERO);
				flatItems.add(f);
			});
		});
		orderForm.setItems(flatItems);
		orderForm.setUpdateItems(true);
		orderForm.setTotal(BigDecimal.ZERO);
		model.addAttribute("orderForm", orderForm);
		model.addAttribute("itemsByCategory", itemsByCategory);
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Agregar orden");
		model.addAttribute("btnAction", "Agregar");
		return "orders/order-form";
	}

	private OrderItemFormDto toOrderItemDto(Product item, int count) {
		OrderItemFormDto o = new OrderItemFormDto();
		o.setItemId(item.getId());
		o.setItemName(item.getName());
		o.setItemPrice(item.getPrice());
		o.setQuantity(0);
		o.setTotal(BigDecimal.ZERO);
		o.setItemSize(item.getSize());
		o.setIndex(count);
		return o;
	}

	@PostMapping("/save")
	public String guardar(@ModelAttribute OrderFormDto orderForm, RedirectAttributes ra) {
		try {
			orderService.saveOrderWithItems(orderForm);
			ra.addFlashAttribute("success", true);
		} catch (IllegalStateException e) {
			ra.addFlashAttribute("success", false);
			ra.addFlashAttribute("msgType", "msg-open-error");
		} catch (EmptyOrderException e) {
			ra.addFlashAttribute("success", false);
			ra.addFlashAttribute("msgType", "custom-error-message");
			ra.addFlashAttribute("msg", e.getMessage());
		}
		return "redirect:/orders";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes ra) {
		
		Order order = orderService.getById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		
		String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
		if (!"ROLE_ADMIN".equals(role)) {
			if (order.getBusinessDay().getStatus() == BusinessDayStatus.CLOSE) {
				ra.addFlashAttribute("success", false);
				ra.addFlashAttribute("msgType", "custom-error-message");
				ra.addFlashAttribute("msg", "Solo el administrador puede editar órdenes pasadas");
				return "redirect:/orders";
			}
		}
		
		List<Product> products = productService.findByStatusTrueOrderByCategoryNameAscNameAsc();

		Map<Category, List<OrderItemFormDto>> itemsByCategory = new LinkedHashMap<>();
		List<OrderItemFormDto> flatItems = new ArrayList<>();
		Map<Long, OrderItem> orderItemMap = order.getItems().stream()
				.collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));
		for (Product p : products) {
			OrderItemFormDto dto = new OrderItemFormDto();
			dto.setIndex(flatItems.size());
			dto.setItemId(p.getId());
			dto.setItemName(p.getName());
			dto.setItemPrice(p.getPrice());
			dto.setItemSize(p.getSize());
			if (orderItemMap.containsKey(p.getId())) {
				OrderItem oi = orderItemMap.get(p.getId());
				dto.setQuantity(oi.getQuantity());
				dto.setTotal(oi.getTotal());
			} else {
				dto.setQuantity(0);
				dto.setTotal(BigDecimal.ZERO);
			}
			itemsByCategory.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(dto);
			flatItems.add(dto);
		}
		OrderFormDto orderForm = new OrderFormDto();
		orderForm.setOrderId(order.getId());
		orderForm.setOrderDescription(order.getDescription());
		orderForm.setOrderUpdatedAt(order.getUpdatedAt());
		orderForm.setStatus(order.getStatus());
		orderForm.setTotal(order.getTotal());
		orderForm.setItems(flatItems);
		orderForm.setUpdateItems(true);
		model.addAttribute("orderForm", orderForm);
		model.addAttribute("itemsByCategory", itemsByCategory);
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Modificar orden");
		model.addAttribute("btnAction", "Actualizar");
		return "orders/order-form";
	}

	@GetMapping("/details/{id}")
	public String detail(@PathVariable Long id, Model model) {
		Order order = orderService.getById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		Map<Category, List<OrderItemFormDto>> itemsByCategory = new LinkedHashMap<>();
		List<OrderItemFormDto> flatItems = new ArrayList<>();
		order.getItems().stream().sorted(Comparator.comparing((OrderItem i) -> i.getProduct().getCategory().getName())
				.thenComparing(i -> i.getProduct().getName())).forEach(item -> {
					OrderItemFormDto dto = new OrderItemFormDto();
					dto.setIndex(flatItems.size());
					dto.setItemId(item.getProduct().getId());
					dto.setItemName(item.getProduct().getName());
					dto.setItemPrice(item.getProduct().getPrice());
					dto.setItemSize(item.getProduct().getSize());
					dto.setQuantity(item.getQuantity());
					dto.setTotal(item.getTotal());
					itemsByCategory.computeIfAbsent(item.getProduct().getCategory(), k -> new ArrayList<>()).add(dto);
					flatItems.add(dto);
				});
		OrderFormDto orderForm = new OrderFormDto();
		orderForm.setOrderId(order.getId());
		orderForm.setOrderDescription(order.getDescription());
		orderForm.setOrderUpdatedAt(order.getUpdatedAt());
		orderForm.setStatus(order.getStatus());
		orderForm.setTotal(order.getTotal());
		orderForm.setItems(flatItems);
		orderForm.setUpdateItems(false);
		model.addAttribute("orderForm", orderForm);
		model.addAttribute("itemsByCategory", itemsByCategory);
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Detalles orden");
		model.addAttribute("action", "showDetails");
		model.addAttribute("btnAction", "Actualizar");
		return "orders/order-form";
	}

	@PostMapping("/{id}/cancel")
	public String eliminar(@PathVariable Long id) {
		Order order = orderService.getById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(OrderStatus.CANCELLED);
		order.setUpdatedAt(LocalDateTime.now());
		order.setTotal(BigDecimal.ZERO);
		orderService.save(order);
		return "redirect:/orders";
	}

	@PostMapping("/{id}/deliver")
	public String deliver(@PathVariable Long id) {
		Order order = orderService.getById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(OrderStatus.COMPLETED);
		order.setUpdatedAt(LocalDateTime.now());
		orderService.save(order);
		return "redirect:/orders";
	}
	
	@GetMapping("/historial")
	public String historial(Model model) {
		model.addAttribute("active", "historial-orders");
		return "orders/orders-historial";
	}

}
