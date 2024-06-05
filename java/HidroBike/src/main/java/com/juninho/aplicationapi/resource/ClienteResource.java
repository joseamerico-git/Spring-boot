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
import com.juninho.aplicationapi.model.Cliente;
import com.juninho.aplicationapi.repository.ClienteRepository;

@RestController
@RequestMapping("/clientes")
public class ClienteResource {
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Cliente> listar() {
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes;

	}

	@GetMapping("/list")
	public ResponseEntity<List<Cliente>> getAllPessoa(@RequestParam(required = false) String nome) {
		try {
			List<Cliente> clientes = new ArrayList<>();

			if (nome == null)
				clienteRepository.findAll().forEach(clientes::add);
			else
				clienteRepository.findByNomeContaining(nome).forEach(clientes::add);

			if (clientes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(clientes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Cliente> getPessoalById(@PathVariable("codigo") long codigo) {
		Optional<Cliente> clienteRecuperado = clienteRepository.findById(codigo);

		if (clienteRecuperado.isPresent()) {
			return new ResponseEntity<>(clienteRecuperado.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente, HttpServletResponse response) {
		Cliente clienteSalvo = clienteRepository.save(cliente);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, cliente.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);

	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Cliente> updateCliente(@PathVariable("codigo") Long codigo, @RequestBody Cliente cliente) {
		Optional<Cliente> clienteSalvo = clienteRepository.findById(codigo);

		if (clienteSalvo.isPresent()) {
			Cliente pes = clienteSalvo.get();
			pes.setNome(cliente.getNome());

			return new ResponseEntity<>(clienteRepository.save(pes), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // retorna 204 no content, deu tudo certo mas não tenho nada pra te retornar
	public void remover(@PathVariable Long codigo) {
		clienteRepository.deleteById(codigo);
	}

}
