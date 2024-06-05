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
import com.juninho.aplicationapi.model.Categoria;
import com.juninho.aplicationapi.repository.CategoriaRepository;

/*
 * @CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api")

@CrossOriginé para configurar origens permitidas.
- a @RestControlleranotação é usada para definir um controlador e indicar que o valor de retorno dos métodos deve ser vinculado ao corpo da resposta da web.
- @RequestMapping("/api")declara que todos os url de Apis no controlador começarão com /api.
- Usamos @Autowiredpara injetar TutorialRepositorybean na variável local.
*/

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Categoria> listar() {
		List<Categoria> categorias = categoriaRepository.findAll();
		/*
		 *
		 * ResponseEntity<?>(retoro) return !categoriaRepository.findAll().isEmpty() ?
		 * ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
		 */
		return categorias;

	}

	@GetMapping("/list")
	public ResponseEntity<List<Categoria>> getAllCategoria(@RequestParam(required = false) String nome) {
		try {
			List<Categoria> categorias = new ArrayList<>();

			if (nome == null)
				categoriaRepository.findAll().forEach(categorias::add);
			else
				categoriaRepository.findByNomeContaining(nome).forEach(categorias::add);

			if (categorias.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(categorias, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {

		Categoria categoriaSalva = categoriaRepository.save(categoria);
		/*
		 * URI uri =
		 * ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		 * .buildAndExpand(categoriaSalva.getCodigo()).toUri();
		 * response.setHeader("Location", uri.toASCIIString());
		 */ publisher.publishEvent(new RecursoCriadoEvent(this, response, categoria.getCodigo()));
		// return ResponseEntity.created(uri).body(categoriaSalva);
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> getTutorialById(@PathVariable("codigo") long codigo) {
		Optional<Categoria> categoriaRecuperada = categoriaRepository.findById(codigo);

		if (categoriaRecuperada.isPresent()) {
			return new ResponseEntity<>(categoriaRecuperada.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		categoriaRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Categoria> updateCategoria(@PathVariable("codigo") long codigo,
			@Valid @RequestBody Categoria categoria) {
		Optional<Categoria> categoriaSalva = categoriaRepository.findById(codigo);

		if (categoriaSalva.isPresent()) {
			Categoria cat = categoriaSalva.get();
			cat.setNome(categoria.getNome());

			return new ResponseEntity<>(categoriaRepository.save(cat), HttpStatus.OK);

		} else {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}

}
