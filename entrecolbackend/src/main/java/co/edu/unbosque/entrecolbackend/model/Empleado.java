package co.edu.unbosque.entrecolbackend.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Empleados")
public class Empleado {

	private @Id Long codigo;
	private @Column(name = "nombreEmpleado", nullable = false) String nombreEmpleado;
	private @Column(name = "fecha_ingreso") Date fecha_ingreso;
	private @Column(name = "arl", nullable = false) String arl;
	private @Column(name = "sueldo", nullable = false) Double sueldo;
	private @Column(name = "email") String email;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private @JoinColumn(name = "cargo", referencedColumnName = "idCargo") Cargo cargo;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private @JoinColumn(name = "pension", referencedColumnName = "idPension") Pension pension;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private @JoinColumn(name = "eps", referencedColumnName = "idEPS") EPS eps;

    @ManyToOne
    @JoinColumn(name = "dependencia_id", nullable = false) // Ajusta el nombre de columna si es diferente en tu base de datos
    private Dependencia dependencia;
	
	public Empleado() {
		// TODO Auto-generated constructor stub
	}

	public Empleado(Long codigo, String nombreEmpleado, Date fecha_ingreso, String arl, Double sueldo, String email,
			Cargo cargo, Pension pension, EPS eps) {
		super();
		this.codigo = codigo;
		this.nombreEmpleado = nombreEmpleado;
		this.fecha_ingreso = fecha_ingreso;
		this.arl = arl;
		this.sueldo = sueldo;
		this.email = email;
		this.cargo = cargo;
		this.pension = pension;
		this.eps = eps;
	}
	

	public Empleado(String nombreEmpleado, Date fecha_ingreso, String arl, Double sueldo, String email, Cargo cargo,
			Pension pension, EPS eps) {
		super();
		this.nombreEmpleado = nombreEmpleado;
		this.fecha_ingreso = fecha_ingreso;
		this.arl = arl;
		this.sueldo = sueldo;
		this.email = email;
		this.cargo = cargo;
		this.pension = pension;
		this.eps = eps;
	}

	public Long getCodigo() {
		return codigo;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public String getArl() {
		return arl;
	}

	public void setArl(String arl) {
		this.arl = arl;
	}

	public Double getSueldo() {
		return sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Pension getPension() {
		return pension;
	}

	public void setPension(Pension pension) {
		this.pension = pension;
	}

	public EPS getEps() {
		return eps;
	}

	public void setEps(EPS eps) {
		this.eps = eps;
	}

}
