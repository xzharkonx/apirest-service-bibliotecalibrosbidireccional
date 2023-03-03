package com.api.rest.biblioteca.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "biblioteca")
public class Biblioteca {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Para está anotación funcione 
	// Añadir la dependencia de spring-boot-starter-validation.
	@NotNull
	private String nombre;
	
	// Con mappedBy le pasaremos el nombre del campo en el cual será
	// mapeado este objeto (como clave foranea digamoslo asi),
	// por lo que indicará que esta no es la propietaria.
	
	// Con cascada indicamos que si eliminamos una biblioteca, 
	// también se eliminaran los libros que trae consigo.
	
	// Una Biblioteca a Muchos Libros.
	@OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL)
	private Set<Libro> libros = new HashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Libro> getLibros() {
		return libros;
	}

	public void setLibros(Set<Libro> libros) {
		this.libros = libros;
		
		// Le mandamos la biblioteca a la que pertenece cada libro.
		for(Libro libro : libros) {
			libro.setBiblioteca(this);
		}
	}
	
	

}
