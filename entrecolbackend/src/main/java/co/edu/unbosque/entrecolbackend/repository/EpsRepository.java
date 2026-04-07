package co.edu.unbosque.entrecolbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Dependencia;
import co.edu.unbosque.entrecolbackend.model.EPS;

public interface EpsRepository extends CrudRepository<EPS, Long>{
	public Optional<EPS> findByNombreEps(String nombreEps);


}
