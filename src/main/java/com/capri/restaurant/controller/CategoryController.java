package com.capri.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.capri.restaurant.model.Category;
import com.capri.restaurant.service.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	private static final String ACTIVE = "categories";
	private static final String TITLE = "Categories";
	
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("categories", categoryService.findAllOrderByNameAsc());
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		return "categories/categories";
	}

	@GetMapping("/new")
	public String nuevo(Model model) {
		model.addAttribute("categoryForm", new Category());
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Agregar categoría");
		return "categories/category-form";
	}

	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute("categoryForm") Category category, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("active", ACTIVE);
			model.addAttribute("title", TITLE);
			model.addAttribute("operation", (category.getId() == null) ? "Agregar categoría" : "Modificar categoría");
			return "categories/category-form";
		}
		Category saveCategory = category;
		if (category.getId() != null) {
			saveCategory = categoryService.getById(category.getId()).orElseThrow(() -> new RuntimeException("Category not found"));
			saveCategory.setName(category.getName());
			saveCategory.setStatus(category.getStatus());
		}
		categoryService.save(saveCategory);
		return "redirect:/categories";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Category categoryForm = categoryService.getById(id).orElseThrow();
		model.addAttribute("categoryForm", categoryForm);
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Modificar categoría");
		return "categories/category-form";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable Long id) {
		categoryService.deleteById(id);
		return "redirect:/categories";
	}
	
}
