package co.edu.unbosque.entrecolbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "Admin")
public class Admin {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private @Column(name = "usuario", nullable = false) String usuario;
	private @Column(name = "contrasena", nullable = false) String contrasena;
	private @Column(name = "email", nullable = false) String email;
	
	public Admin() {
		// TODO Auto-generated constructor stub
	}

	public Admin(String usuario, String contrasena, String email) {
		super();
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", usuario=" + usuario + ", contrasena=" + contrasena + ", email=" + email + "]";
	}
	
	
	
	
}
