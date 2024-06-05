package com.juninho.aplicationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juninho.aplicationapi.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	List<Pessoa> findByAtivo(boolean ativo);

	List<Pessoa> findByNomeContaining(String nome);

}
