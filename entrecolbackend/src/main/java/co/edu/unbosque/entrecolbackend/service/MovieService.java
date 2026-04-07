package co.edu.unbosque.entrecolbackend.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.entrecolbackend.dto.MovieDTO;
import co.edu.unbosque.entrecolbackend.model.Genero;
import co.edu.unbosque.entrecolbackend.model.Movie;
import co.edu.unbosque.entrecolbackend.repository.GeneroRepository;
import co.edu.unbosque.entrecolbackend.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository mRep;

	@Autowired
	private GeneroRepository generoRepository;

	private final Gson gson = new Gson();

	private ExecutorService executor = Executors.newFixedThreadPool(10);

	// Crear una nueva película
	public Movie createMovie(Movie movie) {
		return mRep.save(movie);
	}

	// Obtener una película por ID
	public Optional<Movie> getMovieById(Integer id) {
		return mRep.findById(id);
	}

	// Obtener todas las películas
	public List<Movie> getAllMovies() {
		return mRep.findAll();
	}

	public List<Movie> getTop50Movies(Integer lim) {
		Pageable limit = PageRequest.of(0, lim); // Página 0, tamaño 50
		return mRep.findTop50Movies(limit);
	}

	// Actualizar una película existente
	public Optional<Movie> updateMovie(Integer id, Movie movie) {
		return mRep.findById(id).map(existingMovie -> {
			movie.setId_movie(id);
			return mRep.save(movie);
		});
	}

	// Eliminar una película por ID
	public boolean deleteMovie(Integer id) {
		if (mRep.existsById(id)) {
			mRep.deleteById(id);
			return true;
		}
		return false;
	}

	private static long c = 0;
	private List<Movie> m;
	private List<Genero> g;

	public void readDat(String file) {
		m = new ArrayList<>();
		g = new ArrayList<>();
		ArrayList<String> genders = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("::");

				int number = Integer.parseInt(parts[0]);
				String title = parts[1];
				String year = (title.split("\\(")[title.split("\\(").length - 1]).replaceAll("\\)", "");

				int y = Integer.parseInt(year);
				title = title.split("\\(")[0];

				Movie aux = new Movie(number, title, y);
				String line_g = parts[2];
				line_g = line_g.replace('|', ',');
				String[] genero = line_g.split(",");

				for (int i = 0; i < genero.length; i++) {
					if (genders.contains(genero[i])) {
						for (int j = 0; j < genders.size(); j++) {
							if (genders.get(j).equals(genero[i])) {
								aux.getGeneros().add(g.get(j));
								break;
							}
						}
					} else {
						g.add(new Genero((g.size() + 1), genero[i]));
						generoRepository.save(new Genero((g.size() + 1), genero[i]));
						aux.getGeneros().add(g.get(g.size() - 1));
						genders.add(genero[i]);
					}
				}
				m.add(aux);
				mRep.save(aux);
			}
		} catch (IOException e) {
			System.out.println("Error en la lectura del .dat");
			e.printStackTrace();
		}
	}

	public Map<String, Long> obtenerFrecuenciaPorGenero() {
		return mRep.findAll().stream() // Obtiene todas las películas
				.flatMap(movie -> movie.getGeneros().stream()) // Descompone el conjunto de géneros
				.collect(Collectors.groupingBy(Genero::getGenero, // Agrupa por el nombre del género
						Collectors.counting() // Cuenta las ocurrencias
				));
	}

	public MovieRepository getmRep() {
		return mRep;
	}

	public void setmRep(MovieRepository mRep) {
		this.mRep = mRep;
	}

	public static long getC() {
		return c;
	}

	public static void setC(long c) {
		MovieService.c = c;
	}

	public List<Movie> getM() {
		return m;
	}

	public void setM(List<Movie> m) {
		this.m = m;
	}

	public List<Genero> getG() {
		return g;
	}

	public void setG(List<Genero> g) {
		this.g = g;
	}

	public Movie saveMovie(Movie m) {
		return mRep.save(m);
	}

	public List<Movie> findMoviesByGeneroCount(int cantidad) {
		long totalMovies = mRep.countTotalMovies();
		int chunkSize = (int) (totalMovies / 10);

		List<Callable<List<Movie>>> tasks = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			final int start = i * chunkSize;
			final int end = Math.min(start + chunkSize, (int) totalMovies);

			tasks.add(() -> mRep.findMoviesByGeneroCountInRange(cantidad, start, end));
		}

		try {
			List<Future<List<Movie>>> futures = executor.invokeAll(tasks);
			List<Movie> allMovies = new ArrayList<>();
			for (Future<List<Movie>> future : futures) {
				allMovies.addAll(future.get());
			}

			return allMovies;

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

}
