package co.edu.unbosque.entrecolbackend.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id_movie")
@Entity
@Table(name = "movies")
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id_movie;
	private String titulo;
	private int lanzamiento;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "movies_generos", joinColumns = { @JoinColumn(name = "id_movie") }, inverseJoinColumns = {
			@JoinColumn(name = "id_genero") })
	private Set<Genero> generos = new HashSet<>();

	public Movie() {
		// TODO Auto-generated constructor stub
	}

	public Set<Genero> getGeneros() {
		return generos;
	}

	public Movie(Integer id_movie, String titulo, int lanzamiento) {
		super();
		this.id_movie = id_movie;
		this.titulo = titulo;
		this.lanzamiento = lanzamiento;
	}

	public Movie(String titulo, int lanzamiento) {
		super();
		this.titulo = titulo;
		this.lanzamiento = lanzamiento;
	}

	public void setGeneros(Set<Genero> generos) {
		this.generos = generos;
	}

	public Integer getId_movie() {
		return id_movie;
	}

	public void setId_movie(Integer id_movie) {
		this.id_movie = id_movie;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getLanzamiento() {
		return lanzamiento;
	}

	public void setLanzamiento(int lanzamiento) {
		this.lanzamiento = lanzamiento;
	}

}