package com.nguyenduybinh.Jwt.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nguyenduybinh.Jwt.entities.CustomUserDetails;
import com.nguyenduybinh.Jwt.entities.User;
import com.nguyenduybinh.Jwt.entities.UserRoles;
import com.nguyenduybinh.Jwt.repositories.UserRepository;
import com.nguyenduybinh.Jwt.repositories.UserRolesRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRolesRepository userRolesRepository;
	
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		
		if(!user.isPresent()) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		
		List<UserRoles> userRoles = userRolesRepository.findByUser(user.get());
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		
		if(!CollectionUtils.isEmpty(userRoles)) {
			userRoles.forEach(
					userRole -> {
						GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.getRole().getName());
						grantedAuthorities.add(grantedAuthority);
					}
			);
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails();
		customUserDetails.setUser(user.get());
		customUserDetails.setAuthorities(grantedAuthorities);
		
		return customUserDetails;
	}

}
