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
import com.api.rest.biblioteca.entities.Libro;
import com.api.rest.biblioteca.repository.BibliotecaRepository;
import com.api.rest.biblioteca.repository.LibroRepository;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
	
	@Autowired
	private LibroRepository libroRepository;
	
	@Autowired
	private BibliotecaRepository bibliotecaRepository;
	
	
	// Obtenemos todas las bibliotecas que va a ser por defecto.
	// Le indicamos que vamos a trabajar con paginación a la entidad Biblioteca.
	// Si es que le indicamos la páginación entonces nos traerá los datos
	// en forma de pagína x traera tantos valores y.
	@GetMapping
	public ResponseEntity<Page<Libro>> listarLibros(Pageable pageable){
		return ResponseEntity.ok(libroRepository.findAll(pageable));	
	}
	
	@PostMapping
	public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro){
		
		Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId()); 
		
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!bibliotecaOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		libro.setBiblioteca(bibliotecaOptional.get());
		Libro libroGuardado = libroRepository.save(libro);
		
		// De este Objeto estamos creando una URI que sería el Objeto actual que creamos de biblioteca con su id.
		URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(libroGuardado.getId()).toUri();
		return ResponseEntity.created(ubicacion).body(libroGuardado);
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<Libro> editarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro){
		
		Optional<Libro> libroOptional = libroRepository.findById(id); 
		Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId()); 
		
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!libroOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();	
		}

		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!bibliotecaOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		// Aquí le asignamos el id del objeto que encontro al objeto modificado
		// y luego utilizamos el metodo de save, ya que si esta, solo sobreescribira los valores.
		libro.setBiblioteca(bibliotecaOptional.get());
		libro.setId(libroOptional.get().getId());
		libroRepository.save(libro);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Libro> eliminarLibro(@PathVariable Integer id){
		
		Optional<Libro> libroOptional = libroRepository.findById(id); 
		
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!libroOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();	
		}
		libroRepository.delete(libroOptional.get());
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Integer id){
		
		Optional<Libro> librocaOptional = libroRepository.findById(id); 
		
		// Si no se devolvio algo o si no se encontro nada que devuvelva algo vacio porque no se a podido procesar.
		if (!librocaOptional.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		// Le decimos que la respuesta es 200 OK y que todo esta bien y le pasamos la bibliotecaOptional y ontenemos
		// el objeto, en este caso nos devolvería el JSON.
		return ResponseEntity.ok(librocaOptional.get()); 
	}

}
