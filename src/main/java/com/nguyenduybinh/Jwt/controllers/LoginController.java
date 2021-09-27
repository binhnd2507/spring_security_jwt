package com.nguyenduybinh.Jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenduybinh.Jwt.config.JwtTokenUtil;
import com.nguyenduybinh.Jwt.entities.CustomUserDetails;
import com.nguyenduybinh.Jwt.request.UserRequest;
import com.nguyenduybinh.Jwt.response.UserWithTokenResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping
	public ResponseEntity<UserWithTokenResponse> login(@RequestBody @Validated UserRequest userRequest){
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
					);
			
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			UserWithTokenResponse userWithTokenResponse = UserWithTokenResponse.from(customUserDetails.getUser());
			userWithTokenResponse.setToken(jwtTokenUtil.generateTokenByCustomUserDetails(customUserDetails));
			
			return new ResponseEntity<UserWithTokenResponse>(userWithTokenResponse, HttpStatus.OK);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<UserWithTokenResponse>(new UserWithTokenResponse(), HttpStatus.UNAUTHORIZED);
		}
	}
}
