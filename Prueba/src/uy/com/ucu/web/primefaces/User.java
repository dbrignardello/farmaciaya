package uy.com.ucu.web.primefaces;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@ApplicationScoped
public class User implements Serializable {


    String userName;
    
    public User(){
    	
    }
	
	public String transformSubmitedUserName(){
		return getUserName()+"-transformadoPorBackend";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}