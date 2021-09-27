package com.nguyenduybinh.Jwt.request;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class UserRequest {

	@NotNull
	private String username;
	
	@NotNull
	private String password;
}
