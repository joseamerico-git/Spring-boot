package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	List<Lancamento> findByTipoContaining(String tipo);
}
