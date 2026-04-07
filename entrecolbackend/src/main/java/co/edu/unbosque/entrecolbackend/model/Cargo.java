package co.edu.unbosque.entrecolbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cargo")
public class Cargo {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long idCargo;
	private @Column(name = "cargoNombre", nullable = false) String cargoNombre;

	@ManyToOne
	private @JoinColumn(name = "dependencia_id", nullable = false, referencedColumnName = "idDependencia") Dependencia dependencia;

	public Cargo() {
		// TODO Auto-generated constructor stub
	}

	public Cargo(Long idCargo, String cargoNombre, Dependencia dependencia) {
		super();
		this.idCargo = idCargo;
		this.cargoNombre = cargoNombre;
		this.dependencia = dependencia;
	}

	public Long getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(Long idCargo) {
		this.idCargo = idCargo;
	}

	public String getCargoNombre() {
		return cargoNombre;
	}

	public void setCargoNombre(String cargoNombre) {
		this.cargoNombre = cargoNombre;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	@Override
	public String toString() {
		return "Cargo [idCargo=" + idCargo + ", cargoNombre=" + cargoNombre + ", dependencia=" + dependencia + "]";
	}

}
