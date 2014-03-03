package org.primefaces.examples.view;  

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
  

@ManagedBean
@SessionScoped
public class ValidationBean {  
      
    private String text;  
  
    @Email(message = "must be a valid email")  
    private String email;  
      
    public String getText() {  
        return text;  
    }  
    public void setText(String text) {  
        this.text = text;  
    }  
  
    public String getEmail() {  
        return email;  
    }  
    public void setEmail(String email) {  
        this.email = email;  
    }  
}