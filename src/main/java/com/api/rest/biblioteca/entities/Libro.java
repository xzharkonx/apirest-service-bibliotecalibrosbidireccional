package com.api.rest.biblioteca.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

// Con la propiedad uniqueConstraints le indicamos que el nombre (la columna) tiene que ser única.

@Entity
@Table(name = "libros", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre"})})
public class Libro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// Para está anotación funcione
	// Añadir la dependencia de spring-boot-starter-validation.
	@NotNull
	private String nombre;

	// La opción de LAZY es para la carga peresosa de archivos
	// y que solo lo cargue cuando se lo pedimos.
	
	// EAGER es otra opción pero su función es la carga forzada por lo que
	// va a estar persistiendo todo el rato y nos traera todos los datos
	// de todas las bibliotecas para cada libro y eso es muy pesado.
	
	// Pero LAZY puede tirar error LAZY Initialation exception,
	// error con ApiRest en la serialización y deserialización, para evitarlo
	// usaremos la anotación @JsonProperty con la opción de solo escritura.
	// Para que la Api Rest ignore la propiedad a serializar en una cadena Json.
	// Si no se inicializa en un contexto no Transaccional.
	// Más info en:
	// http://cursohibernate.es/doku.php?id=unidades:06_objetos_validaciones:01_trabajando_objetos
	
	// Con JoinColumn Añadimos la columnna para ese dato la tabla.
	// También este será el nombre del campo o columna adicional donde se mapera la FK
	// por lo que indicará que será la clase propietaria.
	
	// Muchos Libros a Una Biblioteca.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinColumn(name = "biblioteca_id")
	private Biblioteca biblioteca;

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

	public Biblioteca getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(Biblioteca biblioteca) {
		this.biblioteca = biblioteca;
	}

}
