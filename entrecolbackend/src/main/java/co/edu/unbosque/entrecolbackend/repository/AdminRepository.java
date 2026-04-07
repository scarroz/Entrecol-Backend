package co.edu.unbosque.entrecolbackend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.entrecolbackend.model.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long>{
	public Optional<Admin> findByUsuario(String usuario);

}
