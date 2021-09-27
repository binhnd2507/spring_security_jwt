package com.nguyenduybinh.Jwt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenduybinh.Jwt.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
}
