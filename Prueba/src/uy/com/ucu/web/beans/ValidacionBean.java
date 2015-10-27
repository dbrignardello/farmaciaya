package uy.com.ucu.web.beans;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SlideEndEvent;

import uy.com.ucu.web.backoffice.Usuario;
import uy.com.ucu.web.negocio.Farmacia;
import uy.com.ucu.web.negocio.Geolocalizacion;
import uy.com.ucu.web.utilities.GeolocationUtilities;
import uy.com.ucu.web.utilities.MailUtilities;
import uy.com.ucu.web.utilities.SecurityUtilities;
import uy.com.ucu.web.utilities.SessionUtilities;

@ManagedBean(name="validacionBean") 
@SessionScoped
public class ValidacionBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private String codigo;
	private String resultado;

	
	private EntityManager entityManager;
	
	
	public ValidacionBean(){		
		
	}
	
	public void Validar(ComponentSystemEvent event){
		setEntityManager(Persistence.createEntityManagerFactory("prueba").createEntityManager());		
		beginTransaction();
		try{
			//Obtener usuario y marcarlo como validado
			Usuario u = getEntityManager().createNamedQuery("Usuario.findByCode", Usuario.class).setParameter("codigo",codigo).getSingleResult();
			u.setValidado(true);
			this.setResultado("Su e-Mail ha sido validado. Redirigiendo a Login...");
			
		}catch(Exception e){
			this.setResultado("No se ha podido realizar la validacion. Parametro inesperado.");
		}
		endTransaction();
	}
	
	public void beginTransaction(){
		getEntityManager().getTransaction().begin();
	}
	
	public void endTransaction(){
		getEntityManager().getTransaction().commit();
	}
	

	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	

	
	
	

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
}

