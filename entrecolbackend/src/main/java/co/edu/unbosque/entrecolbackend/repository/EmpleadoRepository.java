package co.edu.unbosque.entrecolbackend.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {

	public List<Empleado> findAll();
	public Optional<Empleado> findByCodigo(Long codigo);

}
