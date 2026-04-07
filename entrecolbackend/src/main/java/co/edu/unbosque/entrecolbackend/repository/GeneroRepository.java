package co.edu.unbosque.entrecolbackend.repository;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Genero;

public interface GeneroRepository extends CrudRepository<Genero, Integer>{

	public Genero findByGenero(String genero);
}
