package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByUserNameContaining(String userName);
}
