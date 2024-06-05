package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	List<Produto> findByNomeContaining(String nome);
}
