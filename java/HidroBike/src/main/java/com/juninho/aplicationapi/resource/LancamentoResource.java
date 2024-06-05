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
import com.juninho.aplicationapi.model.Lancamento;
import com.juninho.aplicationapi.model.Lancamento;
import com.juninho.aplicationapi.repository.LancamentoRepository;
import com.juninho.aplicationapi.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {


	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Lancamento> listar() {
		List<Lancamento> lancamentos = lancamentoRepository.findAll();
		/*
		 *
		 * ResponseEntity<?>(retoro) return !categoriaRepository.findAll().isEmpty() ?
		 * ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
		 */
		return lancamentos;

	}

	@GetMapping("/list")
	public ResponseEntity<List<Lancamento>> getAllCategoria(@RequestParam(required = false) String tipo) {
		try {
			List<Lancamento> lancamentos = new ArrayList<>();

			if (tipo == null)
				lancamentoRepository.findAll().forEach(lancamentos::add);
			else
				lancamentoRepository.findByTipoContaining(tipo).forEach(lancamentos::add);

			if (lancamentos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(lancamentos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		/*
		 * URI uri =
		 * ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		 * .buildAndExpand(categoriaSalva.getCodigo()).toUri();
		 * response.setHeader("Location", uri.toASCIIString());
		 */ publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));
		// return ResponseEntity.created(uri).body(categoriaSalva);
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> getTutorialById(@PathVariable("codigo") long codigo) {
		Optional<Lancamento> categoriaRecuperada = lancamentoRepository.findById(codigo);

		if (categoriaRecuperada.isPresent()) {
			return new ResponseEntity<>(categoriaRecuperada.get(), HttpStatus.OK);
		} else {
		//	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Lancamento> updateCategoria(@PathVariable("codigo") long codigo,@Valid
			@RequestBody Lancamento categoria) {
		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);

		if (lancamentoSalvo.isPresent()) {
			Lancamento lanc = lancamentoSalvo.get();
			//lanc.setNome(categoria.getNome());

			return new ResponseEntity<>(lancamentoRepository.save(lanc), HttpStatus.OK);

		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}
	
	

}
