package co.edu.unbosque.entrecolbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Pension;

public interface PensionRepository extends CrudRepository<Pension, Long>{

	public Optional<Pension> findByNombrePension(String nombrePension);

}
