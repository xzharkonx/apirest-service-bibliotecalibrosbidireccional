package com.api.rest.biblioteca.repository;

import com.api.rest.biblioteca.entities.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

// Jpa sirve para indicar que vamos a utilizar la páginación para listar/mostrar los datos.
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Integer> {
}