package com.juninho.aplicationapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juninho.aplicationapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository pessoaRepository;
	
		public void atualizarPropriedadeAtivo(Long codigo, boolean ativo) {
			
		}
		
}
