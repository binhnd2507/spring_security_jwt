package com.nguyenduybinh.Jwt.response;

import com.nguyenduybinh.Jwt.entities.User;

import lombok.Data;

@Data
public class UserResponse {

	private Long id;

	private String username;

	private String phone;
	
	public void fillWith(User user) {
		if(user == null) {
			return;
		}
		
		this.id = user.getId();
		this.username = user.getUsername();
		this.phone = user.getPhone();
	}
	
	public static UserResponse from(User user) {
		UserResponse userResponse = new UserResponse();
		userResponse.fillWith(user);
		return userResponse;
	}
}
