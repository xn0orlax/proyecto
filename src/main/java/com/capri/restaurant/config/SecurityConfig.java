package com.capri.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, AccessDeniedHandler customAccessDeniedHandler) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/css/**", "/js/**").permitAll()
						.requestMatchers("/admin/**", "/products/**", "/businessday/**", "/categories/**", "/sizes/**").hasRole("ADMIN")
						.requestMatchers("/orders/**").hasAnyRole("ADMIN", "USER").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/orders", true).permitAll())
				.exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler))
				.logout(logout -> logout.logoutSuccessUrl("/login?logout"));

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
