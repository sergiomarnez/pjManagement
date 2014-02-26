package com.borislam.domain;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.Size;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;

import com.borislam.config.SpringMongoConfig;

@Document(collection = "cliente")
@ManagedBean
@RequestScoped
public class Cliente implements Serializable{

	@Id
	private String nif;

	@Size(min=2)
	private String nombre;
	
	@Size(min=2)
	private String direccion;
	
	@Size(min=2)
	private String provincia;
	
	public Cliente(){
		
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String id) {
		this.nif = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public void crearCliente(ActionEvent event){	
		System.out.println("Creo Cliente");
		
		Cliente c = this;

			ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			System.out.println("NuevaFactura NO vacia : "+c.getNif());
			mongoOperation.save(c);
			c = new Cliente();
		
	}
}
