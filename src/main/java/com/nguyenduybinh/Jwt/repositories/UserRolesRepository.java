package com.nguyenduybinh.Jwt.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenduybinh.Jwt.entities.User;
import com.nguyenduybinh.Jwt.entities.UserRoles;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long>{

	List<UserRoles> findByUser(User user);
}
