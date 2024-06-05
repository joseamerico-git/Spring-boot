package com.juninho.aplicationapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.juninho.aplicationapi.model.Categoria;
import com.juninho.aplicationapi.repository.PessoaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private PessoaRepository categoriaRepository;
	/*
	public Categoria atualizarPessoa(Long codigo,Categoria categoria) {
		
		Optional<Categoria> categoriaSalva = categoriaRepository.findById(codigo);

		if (categoriaSalva.isPresent()) {
			Categoria cat = categoriaSalva.get();
			cat.setNome(categoria.getNome());

			return new ResponseEntity<>(categoriaRepository.save(cat), HttpStatus.OK);

		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
		
		
	}
	*/

}
