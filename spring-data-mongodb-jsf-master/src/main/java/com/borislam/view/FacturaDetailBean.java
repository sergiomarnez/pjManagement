package com.borislam.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.borislam.config.SpringMongoConfig;
import com.borislam.domain.Cliente;
import com.borislam.domain.Coche;
import com.borislam.domain.Detail;
import com.borislam.domain.Factura;
import com.borislam.domain.Pricing;
import com.borislam.domain.Product;

@Component
@ManagedBean
@SessionScoped
public class FacturaDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FacturaDetailBean(){

	}

	private Coche coche;
	private Factura factura;
	private String newConcepto;	
	private Cliente selectedClient;

	public void initFactura(){
		if (factura==null){
			factura = new Factura();
		}
		
		if (coche==null){
			coche = new Coche();
		}
	}

	public Coche getCoche(){
		return coche;
	}

	public void setCoche(Coche coche){
		this.coche = coche;
	}

	public Cliente getSelectedClient() {
		return selectedClient;
	}

	public void setSelectedClient(Cliente selectedClient) {
		this.selectedClient = selectedClient;
	}

	public String[] getPronviciasList() {
		return pronviciasList;
	}

	public void setPronviciasList(String[] pronviciasList) {
		this.pronviciasList = pronviciasList;
	}

	public String getNewConcepto() {
		return newConcepto;
	}

	public void setNewConcepto(String newConcepto) {
		this.newConcepto = newConcepto;
	}


	public Factura getfactura() {
		return factura;
	}

	public void setfactura(Factura factura) {
		this.factura = factura;
	}


	public void  saludaCoche(ActionEvent event) {
		System.out.println("Hola coche");
	}

	public void  saludaCliente(ActionEvent event) {
		System.out.println("Hola cliente");
	}

	public void  saludaFactura(ActionEvent event) {
		System.out.println("Hola factura");
	}

	public void  añadirConceptos(ActionEvent event) {
		/*Factura f = (Factura)event.getComponent().getAttributes().get("fact");
		
		List<String> conceptos = f.getConceptos();
		if (CollectionUtils.isEmpty(conceptos)) {
			f.setConceptos(new ArrayList<String>());
		}
		f.getConceptos().add(this.newConcepto);
*/
	}

	public void crearFactura(ActionEvent event){	

		Factura f = (Factura)event.getComponent().getAttributes().get("fact");
		if(selectedClient!=null){

			if(f==null){
				System.out.println("factura vacia ");
			}else{
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
	}

	public List<Cliente> completeClient(String query) {  
		List<Cliente> suggestions = new ArrayList<Cliente>();  

		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		List<Cliente> clientes = mongoOperation.findAll(Cliente.class);

		for(Cliente c : clientes) {  
			if(c.getNif().startsWith(query))  
				suggestions.add(c);  
		}  

		return suggestions;  
	} 

	public Converter getConversorClientes() {
		return new Converter() {

			public Object getAsObject(FacesContext arg0, UIComponent arg1,
					String arg2) throws ConverterException {
				if (arg2.trim().equals("") || arg2 == null) {
					return null;
				}
				String number = String.valueOf(arg2);
				ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
				MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(number));

				Cliente c = mongoOperation.findOne(query, Cliente.class);

				//List<Cliente> clientes = mongoOperation.findAll(Cliente.class);
				//Cliente c = clienteDAO.get(Cliente.class, number);
				return c;
			}

			public String getAsString(FacesContext arg0, UIComponent arg1,
					Object arg2) throws ConverterException {
				if (arg2 == null || arg2.equals("")) {
					return "";
				} else {
					return ((Cliente) arg2).getNif() + "";
				}
			}
		};
	}

	private String[] pronviciasList = {"Álava",
			"Albacete",
			"Alicante",
			"Almería",
			"Asturias",
			"Ávila",
			"Badajoz",
			"Barcelona",
			"Burgos",
			"Cáceres",
			"Cádiz",
			"Cantabria",
			"Castellón/Castelló",
			"Ceuta",
			"Ciudad Real",
			"Córdoba",
			"Cuenca",
			"Girona",
			"Las Palmas",
			"Granada",
			"Guadalajara",
			"Guipúzcoa",
			"Huelva",
			"Huesca",
			"Illes Balears",
			"Jaén",
			"A Coruña",
			"La Rioja",
			"León",
			"Lleida",
			"Lugo",
			"Madrid",
			"Málaga",
			"Melilla",
			"Murcia",
			"Navarra",
			"Ourense",
			"Palencia",
			"Pontevedra",
			"Salamanca",
			"Segovia",
			"Sevilla",
			"Soria",
			"Tarragona",
			"Santa Cruz de Tenerife",
			"Teruel",
			"Toledo",
			"Valencia",
			"Valladolid",
			"Vizcaya",
			"Zamora",
	"Zaragoza"};

}
