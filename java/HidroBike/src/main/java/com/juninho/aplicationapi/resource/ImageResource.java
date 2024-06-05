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
import com.juninho.aplicationapi.model.Image;

import com.juninho.aplicationapi.repository.ImageRepository;

@RestController
@RequestMapping("/images")
public class ImageResource {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Image> listar() {
		List<Image> images = imageRepository.findAll();
		/*
		 *
		 * ResponseEntity<?>(retoro) return !categoriaRepository.findAll().isEmpty() ?
		 * ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
		 */
		return images;

	}

	@GetMapping("/list")
	public ResponseEntity<List<Image>> getAllCategoria(@RequestParam(required = false) String nome) {
		try {
			List<Image> images = new ArrayList<>();

			if (nome == null)
				imageRepository.findAll().forEach(images::add);
			else
				imageRepository.findByNomeContaining(nome).forEach(images::add);

			if (images.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(images, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Image> criar(@Valid @RequestBody Image images, HttpServletResponse response) {

		Image imageSalva = imageRepository.save(images);
		/*
		 * URI uri =
		 * ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		 * .buildAndExpand(categoriaSalva.getCodigo()).toUri();
		 * response.setHeader("Location", uri.toASCIIString());
		 */ publisher.publishEvent(new RecursoCriadoEvent(this, response, images.getId()));
		// return ResponseEntity.created(uri).body(categoriaSalva);
		return ResponseEntity.status(HttpStatus.CREATED).body(imageSalva);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Image> getImageId(@PathVariable("id") long id) {
		Optional<Image> imageRecuperada = imageRepository.findById(id);

		if (imageRecuperada.isPresent()) {
			return new ResponseEntity<>(imageRecuperada.get(), HttpStatus.OK);
		} else {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		imageRepository.deleteById(codigo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Image> updateCategoria(@PathVariable("id") long id,
			@Valid @RequestBody Image image) {
		Optional<Image> imageSalva = imageRepository.findById(id);

		if (imageSalva.isPresent()) {
			Image img = imageSalva.get();
			// lanc.setNome(categoria.getNome());

			return new ResponseEntity<>(imageRepository.save(img), HttpStatus.OK);

		} else {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new EmptyResultDataAccessException(1);
		}
	}

}
