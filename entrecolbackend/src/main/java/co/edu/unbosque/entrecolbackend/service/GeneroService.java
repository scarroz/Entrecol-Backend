package co.edu.unbosque.entrecolbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.entrecolbackend.model.Genero;
import co.edu.unbosque.entrecolbackend.repository.GeneroRepository;

@Service
public class GeneroService {

	@Autowired
	private GeneroRepository gRep;
	
	public Genero saveGenero(Genero g) {
		return gRep.save(g);
	}
}
