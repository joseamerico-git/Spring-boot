package com.juninho.aplicationapi.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.juninho.aplicationapi.evento.RecursoCriadoEvent;
import com.juninho.aplicationapi.model.Pessoa;
import com.juninho.aplicationapi.repository.PessoaRepository;
import com.juninho.aplicationapi.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResouce {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Pessoa> listar() {
		List<Pessoa> pessoas = pessoaRepository.findAll();
		return pessoas;

	}

	@GetMapping("/list")
	public ResponseEntity<List<Pessoa>> getAllPessoa(@RequestParam(required = false) String nome) {
		try {
			List<Pessoa> pessoas = new ArrayList<>();

			if (nome == null)
				pessoaRepository.findAll().forEach(pessoas::add);
			else
				pessoaRepository.findByNomeContaining(nome).forEach(pessoas::add);

			if (pessoas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(pessoas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> getPessoalById(@PathVariable("codigo") long codigo) {
		Optional<Pessoa> pessoaRecuperada = pessoaRepository.findById(codigo);

		if (pessoaRecuperada.isPresent()) {
			return new ResponseEntity<>(pessoaRecuperada.get(), HttpStatus.OK);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
			
		}
	}

	@GetMapping("/pessoas/{ativo}")
	public ResponseEntity<List<Pessoa>> findByPublished(@PathVariable boolean ativo) {
		try {
			List<Pessoa> pessoa = pessoaRepository.findByAtivo(ativo);

			if (pessoa.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				//throw new EmptyResultDataAccessException(1);
			}
			return new ResponseEntity<>(pessoa, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {

		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> updatePessoa(@PathVariable("codigo") Long codigo, @RequestBody Pessoa pessoa) {
		return findOptionalPessoaCod(codigo, pessoa);
	}

	private ResponseEntity<Pessoa> findOptionalPessoaCod(Long codigo, Pessoa pessoa) {
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);

		if (pessoaSalva.isPresent()) {
			Pessoa pes = pessoaSalva.get();
			pes.setNome(pessoa.getNome());
			pes.setAtivo(pessoa.isAtivo());

			return new ResponseEntity<>(pessoaRepository.save(pes), HttpStatus.OK);

		} else {
		//	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}
	
	@PutMapping("/{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Pessoa> atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
	
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);
	
		
		if(pessoaSalva.isPresent()) {
			Pessoa _pessoa = pessoaSalva.get();
			_pessoa.setAtivo(ativo);
			return new ResponseEntity<>(pessoaRepository.save(_pessoa), HttpStatus.OK);
		}else {
			
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
		
		
	}
	
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		pessoaRepository.deleteById(codigo);
	}

}
