package com.borislam.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.CollectionUtils;

import com.borislam.config.SpringMongoConfig;
import com.borislam.domain.Factura;
import com.mongodb.BasicDBObject;

@ManagedBean
@SessionScoped
public class UtilDaoBean implements Serializable {
	
	public UtilDaoBean(){
		
	}	
	
	private Factura selectedFactura;

	public Factura getSelectedFactura() {
		return selectedFactura;
	}

	public void setSelectedFactura(Factura selectedFactura) {
		this.selectedFactura = selectedFactura;
	}
	
	public void updateSelectedFactura(ActionEvent event){			

		System.out.println("Actualizando Factura: ");
		System.out.println("Actualizando Factura: "+selectedFactura.getId());

		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");			

		//mongoOperation.remove(selectedFactura);		

		BasicDBObject newDocument = new BasicDBObject();
		BasicDBObject newDocument2 = new BasicDBObject();
		newDocument2.append("nif", selectedFactura.getNif())
		.append("direccion", selectedFactura.getDireccion())
		.append("fecha", selectedFactura.getFecha())
		.append("cantidad", selectedFactura.getCantidad())
		.append("pagada", selectedFactura.getPagada());
		newDocument.append("$set", newDocument2);
	 
		BasicDBObject searchQuery = new BasicDBObject().append("_id", selectedFactura.getId());

		mongoOperation.getCollection("factura").update(searchQuery, newDocument);

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Update Successfully!"));
	}
	

	

}
