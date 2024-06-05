package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	List<Categoria> findByNomeContaining(String nome);

}
