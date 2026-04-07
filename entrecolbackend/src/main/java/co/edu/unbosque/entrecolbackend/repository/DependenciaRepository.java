package co.edu.unbosque.entrecolbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Dependencia;

public interface DependenciaRepository extends CrudRepository<Dependencia, Long> {

	public Optional<Dependencia> findByNombreDependencia(String nombreDependencia);

}
