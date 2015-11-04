package uy.com.ucu.web.backoffice;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import uy.com.ucu.web.negocio.ItemInventario;
import uy.com.ucu.web.negocio.Pedido;

/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@XmlRootElement
@NamedQueries({
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
@NamedQuery(name="Usuario.findByCode", query="SELECT u FROM Usuario u WHERE u.codigo = :codigo"),
@NamedQuery(name="Usuario.findByUsername", query="SELECT u FROM Usuario u WHERE u.username = :username"),
@NamedQuery(name="Usuario.findByEmail", query="SELECT u FROM Usuario u WHERE u.email = :email"),
@NamedQuery(name="Usuario.deleteByUsername", query="DELETE FROM Usuario u WHERE u.username = :username"),
@NamedQuery(name="Usuario.activate", query="UPDATE Usuario u SET u.validado = true WHERE u.username = :username"),
@NamedQuery(name="Usuario.control", query="SELECT u FROM Usuario u WHERE u.username = :username and u.password = :password and u.validado = true")
})

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="idusuario")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private
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
	
	@Column(name="codigo")
	String codigo;
	
	@Column(name="validado")
	Boolean validado;
	
	//bi-directional many-to-one association to Pedido
	@OneToMany(mappedBy="usuario")
	private List<Pedido> pedidos;
	
	public Usuario() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((celular == null) ? 0 : celular.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((nombreCompleto == null) ? 0 : nombreCompleto.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (celular == null) {
			if (other.celular != null) {
				return false;
			}
		} else if (!celular.equals(other.celular)) {
			return false;
		}
		if (direccion == null) {
			if (other.direccion != null) {
				return false;
			}
		} else if (!direccion.equals(other.direccion)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (nombreCompleto == null) {
			if (other.nombreCompleto != null) {
				return false;
			}
		} else if (!nombreCompleto.equals(other.nombreCompleto)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Boolean getValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public int getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}

}