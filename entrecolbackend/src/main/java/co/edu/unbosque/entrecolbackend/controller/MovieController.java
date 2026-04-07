package co.edu.unbosque.entrecolbackend.controller;

import co.edu.unbosque.entrecolbackend.model.Movie;
import co.edu.unbosque.entrecolbackend.service.MovieService;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/movies")
@Transactional
public class MovieController {

	@Autowired
	private MovieService movieService;

	@PostMapping(path = "createPelicula")
	public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
		Movie result = movieService.createMovie(movie);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
		return movieService.getMovieById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("getMovies/{id}")
	public ResponseEntity<List<Movie>> getTop50Movies(@PathVariable Integer id) {
	    List<Movie> result = movieService.getTop50Movies(id);
	    return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<List<Movie>> getAllMovies() {
		List<Movie> result = movieService.getAllMovies();
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movie) {
		return movieService.updateMovie(id, movie).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
		boolean deleted = movieService.deleteMovie(id);
		return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
	@GetMapping("/frecuencia-genero")
	public ResponseEntity<Map<String, Long>> obtenerFrecuenciaPorGenero() {
	    Map<String, Long> frecuenciaGenero = movieService.obtenerFrecuenciaPorGenero();
	    return ResponseEntity.ok(frecuenciaGenero);
	}
	@GetMapping("/by-genero-count/{cantidad}")
	public List<Movie> getMoviesByGeneroCount(@PathVariable int cantidad){
		return movieService.findMoviesByGeneroCount(cantidad);
	}

}
