package com.nguyenduybinh.Jwt.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nguyenduybinh.Jwt.services.UserDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(username -> userRepository
//					.findByUsername(username)
//					.orElseThrow(
//							() -> new EntityNotFoundException(
//									String.format("User with username: %s, not found", username)
//								)
//							)
//					).passwordEncoder(passwordEncoder());
		
		auth
			.userDetailsService(userDetailsServiceImpl);
//			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enable cors and disable csrf
		http = http.cors().and().csrf().disable();
		
		// Set session management to stateless
		http = http
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and();
		
		// Set unauthorized requests exception handler
		http = http
				.exceptionHandling()
				.authenticationEntryPoint(
						(request, response, ex) -> {
							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
						}
				)
				.and();
		
		// Set permission endpoints
		setPermission(http);
		
		// Add Jwt token filter
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	private void setPermission(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			// Our public endpoints
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			
			// Only Admin can access this api
			.antMatchers("/api/admin").hasRole("ADMIN")
			
			// Admin and User can access this api
			.antMatchers("/api/user").hasAnyRole("ADMIN", "USER")
			
			// Our private endpoints
			.anyRequest().authenticated();
	}
}
