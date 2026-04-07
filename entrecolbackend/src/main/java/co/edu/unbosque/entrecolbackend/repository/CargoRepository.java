package co.edu.unbosque.entrecolbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Cargo;
import co.edu.unbosque.entrecolbackend.model.Dependencia;

public interface CargoRepository extends CrudRepository<Cargo, Long>{
	public Optional<Cargo> findByCargoNombre(String cargoNombre);
	public Optional<Cargo> findByCargoNombreAndDependencia(String cargoNombre, Dependencia dependencia);

}
