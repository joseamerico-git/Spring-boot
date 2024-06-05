package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	List<Cliente> findByNomeContaining(String nome);
}
