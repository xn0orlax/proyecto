package com.capri.restaurant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.capri.restaurant.dto.ProductForm;
import com.capri.restaurant.model.Category;
import com.capri.restaurant.model.Product;
import com.capri.restaurant.model.Size;
import com.capri.restaurant.service.CategoryService;
import com.capri.restaurant.service.ProductService;
import com.capri.restaurant.service.SizeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private SizeService sizeService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("productsByCategory", productService.findProductsGroupedByCategoryOrderByProductNameAsc());
		model.addAttribute("active", "products");
		model.addAttribute("title", "Productos");
		return "products/products";
	}

	@GetMapping("/new")
	public String nuevo(Model model) {
		ProductForm productForm = new ProductForm();
		List<Size> sizes = sizeService.findAll();
		Size defaultSize = sizes.stream().findFirst().orElse(null);
		if (defaultSize != null) {
			productForm.setSizeId(defaultSize.getId());
		}
		List<Category> categories = categoryService.findByStatus(true);
		Category defaultCategory = categories.stream().findFirst().orElse(null);
		if (defaultCategory != null) {
			productForm.setCategoryId(defaultCategory.getId());
		}
		model.addAttribute("productForm", productForm);
		model.addAttribute("sizes", sizes);
		model.addAttribute("categories", categories);
		model.addAttribute("active", "products");
		model.addAttribute("title", "Productos");
		model.addAttribute("operation", "Agregar producto");
		return "products/product-form";
	}

	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute("productForm") ProductForm productFormDto, BindingResult result, Model model, RedirectAttributes ra) {
		if (result.hasErrors()) {
			model.addAttribute("sizes", sizeService.findAll());
			model.addAttribute("categories", categoryService.findByStatus(true));
			model.addAttribute("active", "products");
			model.addAttribute("title", "Productos");
			model.addAttribute("operation", productFormDto.getProductId() == null ? "Agregar producto" : "Modificar producto");
			return "products/product-form";
		}
		Product product = new Product();
		product.setId(productFormDto.getProductId());
		product.setName(productFormDto.getProductName());
		product.setDescription(productFormDto.getProductDescription());
		product.setPrice(productFormDto.getProductPrice());
		product.setStatus(productFormDto.getProductStatus());
		Size size = sizeService.getById(productFormDto.getSizeId()).orElseThrow(() -> new RuntimeException("Size not found"));
		product.setSize(size);
		Category category = categoryService.getById(productFormDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
		product.setCategory(category);
		productService.save(product);
		return "redirect:/products";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Product product = productService.findById(id).orElseThrow();
		ProductForm productForm = new ProductForm();
		productForm.setProductId(product.getId());
		productForm.setProductName(product.getName());
		productForm.setProductDescription(product.getDescription());
		productForm.setProductPrice(product.getPrice());
		productForm.setProductStatus(product.getStatus());
		productForm.setSizeId(product.getSize().getId());
		productForm.setCategoryId(product.getCategory().getId());
		
		model.addAttribute("productForm", productForm);
		model.addAttribute("sizes", sizeService.findAll());
		model.addAttribute("categories", categoryService.findByStatus(true));
		model.addAttribute("active", "products");
		model.addAttribute("title", "Productos");
		model.addAttribute("operation", "Modificar producto");
		return "products/product-form";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable Long id) {
		productService.delete(id);
		return "redirect:/products";
	}
}
