package com.api.rest.biblioteca.controllers;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.rest.biblioteca.entities.Biblioteca;
import com.api.rest.biblioteca.repository.BibliotecaRepository;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {
	
	@Autowired
	private BibliotecaRepository bibliotecaRepository;
	
	
	// Obtenemos todas las bibliotecas que va a ser por defecto.
	// Le indicamos que vamos a trabajar con paginación a la entidad Biblioteca.
	// Si es que le indicamos la páginación entonces nos traerá los datos
	// en forma de pagína x traera tantos valores y.
	@GetMapping
	public ResponseEntity<Page<Biblioteca>> listarBibliotecas(Pageable pageable){
		return ResponseEntity.ok(bibliotecaRepository.findAll(pageable));
	}
	
	@PostMapping
	public ResponseEntity<Biblioteca> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca){
		
		Biblioteca bibliotecaGuardada = bibliotecaRepository.save(biblioteca);
		
		// De este Objeto estamos creando una URI que sería el Objeto actual que creamos de biblioteca con su id.
		URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(bibliotecaGuardada.getId()).toUri();
		return ResponseEntity.created(ubicacion).body(bibliotecaGuardada);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Biblioteca> editarBiblioteca(@PathVariable Integer id, @Valid @RequestBody Biblioteca biblioteca){
		
		Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id); 
		
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!bibliotecaOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build(); // 422
		}
		
		// Aquí le asignamos el id del objeto que encontro al objeto modificado
		// y luego utilizamos el metodo de save, ya que si esta, solo sobreescribira los valores.
		biblioteca.setId(bibliotecaOptional.get().getId());
		bibliotecaRepository.save(biblioteca);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Biblioteca> eliminarBiblioteca(@PathVariable Integer id){
		
		Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id); 
		
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!bibliotecaOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		bibliotecaRepository.delete(bibliotecaOptional.get());
		return ResponseEntity.noContent().build(); // 204
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Biblioteca> obtenerBibliotecaPorId(@PathVariable Integer id){
		
		Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(id);
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!bibliotecaOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		// Le decimos que la respuesta es 200 OK y que todo esta bien y le pasamos la bibliotecaOptional y obtenemos
		// el objeto, en este caso nos devolvería el JSON.
		return ResponseEntity.ok(bibliotecaOptional.get()); 
	}

}
