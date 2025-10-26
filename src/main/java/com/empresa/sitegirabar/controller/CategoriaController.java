package com.empresa.sitegirabar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.empresa.sitegirabar.model.Categoria;
import com.empresa.sitegirabar.repository.CategoriaRepository;
import com.empresa.sitegirabar.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private TemaRepository temaRepository;
	
	@GetMapping
	public ResponseEntity<List<Categoria>> getAll(){
		return ResponseEntity.ok(categoriaRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Long id){
		return categoriaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Categoria>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(categoriaRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	@PostMapping
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria) {
		
		if (temaRepository.existsById(categoria.getTema().getId())) {
			
			categoria.setId(null);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(categoriaRepository.save(categoria));
		}
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe", null);
	}
	
	@PutMapping
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {
		if (categoriaRepository.existsById(categoria.getId())) {
		
			if (temaRepository.existsById(categoria.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(categoriaRepository.save(categoria));
						
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
				
	}
	
	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	
}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		if(categoria.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		categoriaRepository.deleteById(id);
	}
	
}
