package co.edu.unbosque.entrecolbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pension")
public class Pension {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long idPension;
	private @Column(name = "nombrePension", nullable = false) String nombrePension;
	
	public Pension() {
		// TODO Auto-generated constructor stub
	}

	public Pension(String nombrePension) {
		super();
		this.nombrePension = nombrePension;
	}

	public Long getId() {
		return idPension;
	}

	public void setId(Long id) {
		this.idPension = id;
	}

	public String getNombrePension() {
		return nombrePension;
	}

	public void setNombrePension(String nombrePension) {
		this.nombrePension = nombrePension;
	}

	@Override
	public String toString() {
		return "Pension [id=" + idPension + ", nombrePension=" + nombrePension + "]";
	}
	
	

}
