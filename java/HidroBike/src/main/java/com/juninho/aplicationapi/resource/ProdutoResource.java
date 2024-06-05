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
import com.juninho.aplicationapi.model.Produto;
import com.juninho.aplicationapi.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Produto> listar() {
		List<Produto> produtos = produtoRepository.findAll();
		/*
		 *
		 * ResponseEntity<?>(retoro) return !categoriaRepository.findAll().isEmpty() ?
		 * ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
		 */
		return produtos;

	}

	@GetMapping("/list")
	public ResponseEntity<List<Produto>> getAllCategoria(@RequestParam(required = false) String tipo) {
		try {
			List<Produto> produtos = new ArrayList<>();

			if (tipo == null)
				produtoRepository.findAll().forEach(produtos::add);
			else
				produtoRepository.findByNomeContaining(tipo).forEach(produtos::add);

			if (produtos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(produtos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto, HttpServletResponse response) {

		Produto produtoSalvo = produtoRepository.save(produto);
		/*
		 * URI uri =
		 * ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		 * .buildAndExpand(categoriaSalva.getCodigo()).toUri();
		 * response.setHeader("Location", uri.toASCIIString());
		 */ publisher.publishEvent(new RecursoCriadoEvent(this, response, produto.getCodigo()));
		// return ResponseEntity.created(uri).body(categoriaSalva);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Produto> getProdutoId(@PathVariable("codigo") long codigo) {
		Optional<Produto> categoriaRecuperada = produtoRepository.findById(codigo);

		if (categoriaRecuperada.isPresent()) {
			return new ResponseEntity<>(categoriaRecuperada.get(), HttpStatus.OK);
		} else {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		produtoRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Produto> updateCategoria(@PathVariable("codigo") long codigo,
			@Valid @RequestBody Produto categoria) {
		Optional<Produto> prdoutoSalvo = produtoRepository.findById(codigo);

		if (prdoutoSalvo.isPresent()) {
			Produto prod = prdoutoSalvo.get();
			// lanc.setNome(categoria.getNome());

			return new ResponseEntity<>(produtoRepository.save(prod), HttpStatus.OK);

		} else {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}

}
