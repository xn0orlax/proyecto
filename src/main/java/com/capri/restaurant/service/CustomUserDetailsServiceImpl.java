package com.capri.restaurant.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capri.restaurant.model.User;
import com.capri.restaurant.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password(user.getPassword())
				.roles(user.getRole().name().replace("ROLE_", ""))
				.disabled(!user.isEnabled())
				.build();
	}

}
