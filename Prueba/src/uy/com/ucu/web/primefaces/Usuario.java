package uy.com.ucu.web.primefaces;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.*;

/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@XmlRootElement
@NamedQueries({
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
@NamedQuery(name="Usuario.findByUsername", query="SELECT u FROM Usuario u WHERE u.username = :username"),
@NamedQuery(name="Usuario.control", query="SELECT u FROM Usuario u WHERE u.username = :username and u.password = :password")
})

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="idusuario")
	@GeneratedValue(strategy=GenerationType.AUTO)
	int idusuario;

	@Column(name="password")
	String password;

	@Column(name="username")
	String username;
	
	@Column(name="email")
	String email;

	@Column(name="nombreCompleto")
	String nombreCompleto;
	
	@Column(name="direccion")
	String direccion;
	
	@Column(name="celular")
	String celular;
	
	
	
	
	public Usuario() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public int getIdusuario() {
		return idusuario;
	}



	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	

}