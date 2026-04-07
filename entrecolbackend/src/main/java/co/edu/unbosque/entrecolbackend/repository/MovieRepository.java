package co.edu.unbosque.entrecolbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import co.edu.unbosque.entrecolbackend.model.Movie;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Integer> {

    public List<Movie> findAll(); // Método original
    
    @Query("SELECT m FROM Movie m ORDER BY m.id ASC")
    List<Movie> findTop50Movies(Pageable pageable);

	@Query(value = "SELECT m.* FROM movies m " + "JOIN movies_generos mg ON m.id_movie = mg.id_movie "
			+ "GROUP BY m.id_movie " + "HAVING COUNT(mg.id_genero) = :cantidad "
			+ "LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<Movie> findMoviesByGeneroCountInRange(@Param("cantidad") int cantidad, @Param("offset") int offset,
			@Param("limit") int limit);

	@Query("SELECT COUNT(m) FROM Movie m")
	long countTotalMovies();
}

