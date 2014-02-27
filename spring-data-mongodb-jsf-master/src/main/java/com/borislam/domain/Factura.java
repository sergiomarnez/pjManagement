package com.borislam.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;

import com.borislam.config.SpringMongoConfig;

@Document(collection = "factura")
@ManagedBean
@ViewScoped
public class Factura implements Serializable{

	@Id
	private String id;

	@Size(min=2,max=5)
	private String nif; 
	@Size(min=2,max=5)
	private String direccion;


	private Date fecha;

	@DecimalMin(value= "0.0", message = "Shold not exceed 99.9") 
	private Double cantidad; 
	private Boolean pagada;
	private List<Concepto> conceptos;	

	public Factura(){

	}

	@PostConstruct
	public void initConceptos() {    	
		conceptos = new ArrayList<Concepto>();
		conceptos.add(new Concepto("",Double.valueOf(0)));
	}

	public List<Concepto> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<Concepto> conceptos) {
		this.conceptos = conceptos;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double  getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double  cantidad) {
		this.cantidad = cantidad;
	}
	public Boolean getPagada() {
		return pagada;
	}
	public void setPagada(Boolean pagada) {
		this.pagada = pagada;
	}

	public void crearFactura(ActionEvent event){	
		System.out.println("asdf");
		Cliente selectedClient = (Cliente)event.getComponent().getAttributes().get("cliente");
		//Factura f = (Factura)event.getComponent().getAttributes().get("fact");
		Factura f = this;
		System.out.println("factura NO vacia : "+f.getNif());
		if(selectedClient!=null){

			ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			System.out.println("factura NO vacia : "+f.getNif());
			Factura factura = f;
			Random randomGenerator = new Random();
			String id = "Factura"+String.valueOf(randomGenerator.nextInt(10000));
			f.setNif(selectedClient.getNif());
			f.setDireccion(selectedClient.getDireccion());
			f.setId(id);
			mongoOperation.save(factura);

		}
	}

	public void addConcepto() {
		conceptos.add(new Concepto("",Double.valueOf(0)));
	}
	
	public void deleteConcepto(ActionEvent event) {
		Concepto selectedConcept = (Concepto)event.getComponent().getAttributes().get("concepto");
		System.out.println("Borrando el concepto: "+selectedConcept.getConcepto());
		conceptos.remove(selectedConcept);
		/*for(Concepto p : conceptos){
			if(selectedConcept.getConcepto().equals(p.getConcepto())&&selectedConcept.getImporte()==p.getImporte()){
				
			}
		}*/
	}

}


