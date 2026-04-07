package co.edu.unbosque.entrecolbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Dependencia")
public class Dependencia {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long idDependencia;
	private @Column(name = "nombreDependencia", nullable = false) String nombreDependencia;

	public Dependencia() {
		// TODO Auto-generated constructor stub
	}

	public Dependencia(String nombreDependencia) {
		super();
		this.nombreDependencia = nombreDependencia;
	}

	public Long getId() {
		return idDependencia;
	}

	public void setId(Long id) {
		this.idDependencia = id;
	}

	public String getNombreDependencia() {
		return nombreDependencia;
	}

	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}

	@Override
	public String toString() {
		return "Dependencia [id=" + idDependencia + ", nombreDependencia=" + nombreDependencia + "]";
	}
	

}
