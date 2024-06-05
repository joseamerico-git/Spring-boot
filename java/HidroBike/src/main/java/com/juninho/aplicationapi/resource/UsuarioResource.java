package com.juninho.aplicationapi.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import com.juninho.aplicationapi.model.User;
import com.juninho.aplicationapi.model.User;
import com.juninho.aplicationapi.repository.ClienteRepository;
import com.juninho.aplicationapi.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UsuarioResource {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<User> listar() {
		List<User> users = userRepository.findAll();
		return users;

	}

	@GetMapping("/list")
	public ResponseEntity<List<User>> getAllUser(@RequestParam(required = false) String userName) {
		try {
			List<User> usuarios = new ArrayList<>();

			if (userName == null)
				userRepository.findAll().forEach(usuarios::add);
			else
				userRepository.findByUserNameContaining(userName).forEach(usuarios::add);

			if (usuarios.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(usuarios, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<User> getPessoalById(@PathVariable("codigo") long codigo) {
		Optional<User> userRecuperado = userRepository.findById(codigo);

		if (userRecuperado.isPresent()) {
			return new ResponseEntity<>(userRecuperado.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<User> criar(@Valid @RequestBody User user, HttpServletResponse response) {
		User clienteSalvo = userRepository.save(user);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, user.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);

	}

	@PutMapping("/{codigo}")
	public ResponseEntity<User> updateUser(@PathVariable("codigo") Long codigo, @RequestBody User user) {
		Optional<User> userSalvo = userRepository.findById(codigo);

		if (userSalvo.isPresent()) {
			User pes = userSalvo.get();
			pes.setUserName(user.getUserName());

			return new ResponseEntity<>(userRepository.save(pes), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		userRepository.deleteById(codigo);
	}

}
