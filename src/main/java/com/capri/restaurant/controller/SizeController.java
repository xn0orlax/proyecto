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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.capri.restaurant.model.Size;
import com.capri.restaurant.service.SizeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/sizes")
public class SizeController {
	
	@Autowired
	private SizeService sizeService;
	
	private static final String ACTIVE = "sizes";
	private static final String TITLE = "Sizes";
	
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("sizes", sizeService.findAllOrderByNameAsc());
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		return "sizes/sizes";
	}

	@GetMapping("/new")
	public String nuevo(Model model) {
		model.addAttribute("sizeForm", new Size());
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Agregar tamaño");
		return "sizes/size-form";
	}

	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute("sizeForm") Size sizeForm, BindingResult result, Model model, RedirectAttributes ra) {
		if (result.hasErrors()) {
			model.addAttribute("active", ACTIVE);
			model.addAttribute("title", TITLE);
			model.addAttribute("operation", (sizeForm.getId() == null) ? "Agregar tamaño" : "Modificar tamaño");
			return "sizes/size-form";
		}
		sizeService.save(sizeForm);
		return "redirect:/sizes";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Size sizeForm = sizeService.getById(id).orElseThrow();
		model.addAttribute("sizeForm", sizeForm);
		model.addAttribute("active", ACTIVE);
		model.addAttribute("title", TITLE);
		model.addAttribute("operation", "Modificar tamaño");
		return "sizes/size-form";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable Long id) {
		sizeService.delete(id);
		return "redirect:/sizes";
	}
	
}
