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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import com.borislam.config.SpringMongoConfig;
import com.borislam.domain.Concepto;
import com.borislam.domain.Factura;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@ManagedBean
@SessionScoped
public class UtilDaoBean implements Serializable {
	
	public UtilDaoBean(){
		
	}	
	
	private Factura selectedFactura;
	private List<Concepto> deletedConceptos = new ArrayList<Concepto>();

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

		
		Concepto[] concepts = new Concepto[selectedFactura.getConceptos().size()];
		concepts = selectedFactura.getConceptos().toArray(concepts);
		
		

		BasicDBObject newDocument = new BasicDBObject();
		BasicDBObject newDocument2 = new BasicDBObject();
		newDocument2.append("nif", selectedFactura.getNif())
		.append("direccion", selectedFactura.getDireccion())
		.append("fecha", selectedFactura.getFecha())
		.append("cantidad", selectedFactura.getCantidad())
		.append("pagada", selectedFactura.getPagada());
		//.append("conceptos",selectedFactura.getConceptos());
		
		newDocument.append("$set", newDocument2);		
	 
		BasicDBObject searchQuery = new BasicDBObject().append("_id", selectedFactura.getId());
		mongoOperation.getCollection("factura").update(searchQuery, newDocument);


		/*BasicDBObject newDocument3 = new BasicDBObject();
		BasicDBObject newDocument4 = new BasicDBObject();
		newDocument3.append("conceptos",selectedFactura.getConceptos());
		newDocument4.append("$set", newDocument3);
		mongoOperation.getCollection("factura").update(searchQuery, newDocument);*/
		
		/*for(Concepto c : selectedFactura.getConceptos()){
			
			DBObject listItem = new BasicDBObject("conceptos", new BasicDBObject().append("concepto",c.getConcepto()).append("importe", c.getImporte()));
			DBObject updateQuery = new BasicDBObject("$addToSet", listItem);	
			mongoOperation.getCollection("factura").update(searchQuery, updateQuery);
		}*/
		
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Update Successfully!"));
	}
	

	public void addConcepto(ActionEvent event) {
		selectedFactura.getConceptos().add(new Concepto("",Double.valueOf(0)));
	}
	
	public void deleteConcepto(ActionEvent event) {
		Concepto selectedConcept = (Concepto)event.getComponent().getAttributes().get("concepto");
		System.out.println("Borrando el concepto: "+selectedConcept.getConcepto());
		selectedFactura.getConceptos().remove(selectedConcept);
	}

}
