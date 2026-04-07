package co.edu.unbosque.entrecolbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EPS")
public class EPS {
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long idEPS;
	private @Column(name = "nombreEps", nullable = false) String nombreEps;
	
	public EPS() {
		// TODO Auto-generated constructor stub
	}

	public EPS(Long idEPS, String nombreEps) {
		super();
		this.idEPS = idEPS;
		this.nombreEps = nombreEps;
	}

	public Long getidEPS() {
		return idEPS;
	}

	public void setidEPS(Long idEPS) {
		this.idEPS = idEPS;
	}

	public String getNombreEps() {
		return nombreEps;
	}

	public void setNombreEps(String nombreEps) {
		this.nombreEps = nombreEps;
	}
	

}
