package com.capri.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.PostConstruct;

@Controller
public class LoginController {
//	@Autowired
//	PasswordEncoder encoder;
//	
//	@PostConstruct
//	public void test() {
//		System.out.println("Password: [[[[" + encoder.encode("panda1530") + "]]]]");
//		System.out.println("Password: [[[[" + encoder.encode("capri123") + "]]]]");
//	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied() {
		return "403";
	}
	
}
