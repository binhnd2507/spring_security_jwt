package com.nguyenduybinh.Jwt.response;

import com.nguyenduybinh.Jwt.entities.User;

import lombok.Data;

@Data
public class UserWithTokenResponse extends UserResponse{

	private String token;
	
	public static UserWithTokenResponse from(User user) {
		UserWithTokenResponse userWithTokenResponse = new UserWithTokenResponse();
		userWithTokenResponse.fillWith(user);
		return userWithTokenResponse;
	}
}
