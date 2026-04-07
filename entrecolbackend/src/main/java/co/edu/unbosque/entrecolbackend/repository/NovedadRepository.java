package co.edu.unbosque.entrecolbackend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Novedad;

public interface NovedadRepository extends CrudRepository<Novedad, Long> {
	public List<Novedad> findAll();
	
}
