package com.bbva.minibank.infrastructure.security.config;

import com.bbva.minibank.application.services.UserDetailsServiceImpl;
import com.bbva.minibank.infrastructure.security.filters.JwtAuthenticationFilter;
import com.bbva.minibank.infrastructure.security.filters.JwtAuthorizationFilter;
import com.bbva.minibank.infrastructure.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	JwtAuthorizationFilter authorizationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
	                                        AuthenticationManager authenticationManager) throws Exception {
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
		jwtAuthenticationFilter.setFilterProcessesUrl("/login");
		
		return httpSecurity
				       .csrf(AbstractHttpConfigurer::disable)
				       .authorizeHttpRequests(auth -> {
					       auth.requestMatchers("/hello")
					           .permitAll();
					       auth.requestMatchers("/accessUser")
					           .hasRole("USER");
					       auth.requestMatchers("/accessAdmin")
					           .hasRole("ADMIN");
					       auth.requestMatchers("/accessInvited")
					           .hasRole("INVITED");
					       auth.anyRequest()
					           .authenticated();
				       })
				       .sessionManagement(session -> {
					       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				       })
				       .addFilter(jwtAuthenticationFilter)
				       .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				       .build();
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
	                                            PasswordEncoder passwordEncoder) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
		                   .userDetailsService(userDetailsService)
		                   .passwordEncoder(passwordEncoder)
		                   .and()
		                   .build();
	}
}
