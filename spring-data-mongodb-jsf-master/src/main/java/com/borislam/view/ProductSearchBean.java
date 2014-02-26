package com.borislam.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.borislam.config.SpringMongoConfig;
import com.borislam.domain.Cliente;
import com.borislam.domain.Detail;
import com.borislam.domain.Factura;
import com.borislam.domain.Pricing;
import com.borislam.domain.Product;
import com.borislam.service.ProductService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import org.apache.log4j.Logger;

@Component
@ManagedBean
@SessionScoped
public class ProductSearchBean implements Serializable{	

	private static final long serialVersionUID = 1L;
	private Product selectedProduct;
	private Factura selectedFactura;
	private List<String> conceptList;
	
	 private static final String PDF_URL = "http://download.itcuties.com/jsf2/jsf2-download-pdf/itcuties-logo-concept.pdf";
	
    public void addConcept() {
    	conceptList.add(new String());
    }

    public List<String> getConceptList() {
        return conceptList;
    }
    
    public void downloadFile() throws IOException {
    	
    	URL location = ProductSearchBean.class.getProtectionDomain().getCodeSource().getLocation();
    	System.out.println("Actual location: "+location.getPath());
    	File ficheroXLS = new File("C:/Users/pjavier/git/pjManagement/spring-data-mongodb-jsf-master/src/main/webapp/resources/pdf/plantilla.pdf");
    	FacesContext ctx = FacesContext.getCurrentInstance();
    	FileInputStream fis = new FileInputStream(ficheroXLS);
    	byte[] bytes = new byte[1000];
    	int read = 0;

    	if (!ctx.getResponseComplete()) {
    	   String fileName = ficheroXLS.getName();
    	   //String contentType = "application/vnd.ms-excel";
    	   String contentType = "application/pdf";
    	   HttpServletResponse response =
    	   (HttpServletResponse) ctx.getExternalContext().getResponse();

    	   response.setContentType(contentType);

    	   response.setHeader("Content-Disposition",
    	                      "attachment;filename=\"" + fileName + "\"");

    	   ServletOutputStream out = response.getOutputStream();

    	   while ((read = fis.read(bytes)) != -1) {
    	        out.write(bytes, 0, read);
    	   }

    	   out.flush();
    	   out.close();
    	   System.out.println("\nDescargado\n");
    	   ctx.responseComplete();
    	}
    }
    
    public void downloadPdf() throws IOException {
		// Get the FacesContext
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		// Get HTTP response
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		
		// Set response headers
		response.reset();	// Reset the response in the first place
        response.setHeader("Content-Type", "application/pdf");	// Set only the content type
        
		// Open response output stream
		OutputStream responseOutputStream = response.getOutputStream();
		
		// Read PDF contents
		URL url = new URL("http://cran.r-project.org/doc/manuals/r-release/R-data.pdf");
		//File f = new File("/resources/pdf/plantilla.pdf");

        InputStream pdfInputStream = url.openStream();
        
        // Read PDF contents and write them to the output
        byte[] bytesBuffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
        	responseOutputStream.write(bytesBuffer, 0, bytesRead);
        }
        
        // Make sure that everything is out
        responseOutputStream.flush();
         
        // Close both streams
        pdfInputStream.close();
        responseOutputStream.close();
        
        // JSF doc: 
        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated 
        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
        // as soon as the current phase is completed.
        facesContext.responseComplete();
        
	}

	private static Logger logger = Logger.getLogger(ProductSearchBean.class);

	private ProductSearchCriteria criteria = new ProductSearchCriteria();

	private List<Product> productList;
	private List<Factura> facturaList;


	public void initProduct(){
		//nuevaFactura = new Factura();		
	}


	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public Factura getSelectedFactura() {
		return selectedFactura;
	}

	public void setSelectedFactura(Factura selectedFactura) {
		this.selectedFactura = selectedFactura;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public ProductSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(ProductSearchCriteria criteria) {
		this.criteria = criteria;
	}
	@Autowired
	private ProductService productService;

	public void doSearch(ActionEvent event){
		productList= productService.searchByCriteria(criteria);
	}
	
	
	
	public void exportarPdf(ActionEvent event){
		System.out.println("Creando pdf de: ");
		
		System.out.println("2. find - savedFactura : " + selectedFactura.toString());
	}

	public void insertaFactura(ActionEvent event){
		System.out.println("Insertando Factura");

		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Random randomGenerator = new Random();
		String id = "Factura"+String.valueOf(randomGenerator.nextInt(10000));
		String nif = "17762868Z";
		String direccion= "Rosalia de Castro 19";
		Date fecha = new Date();
		Integer cantidad = 1234;
		Boolean pagada = false;
		//Factura factura = new Factura(id,  nif,  direccion,  fecha, cantidad,  pagada);

		Query searchUserQuery = new Query(Criteria.where("cantidad").is(1234));

		//mongoOperation.save(factura);

		// find the saved user again.
		Factura savedFactura = mongoOperation.findOne(searchUserQuery, Factura.class);
		System.out.println("2. find - savedFactura : " + savedFactura.toString());
	}

	public void borraFactura(ActionEvent event){
		System.out.println("Borrando Factura");

		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("cantidad").is(1234));

		Factura f = mongoOperation.findOne(query2, Factura.class);		
		mongoOperation.remove(f);
	}


	public List<Factura> getFacturaList() {

		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC,"fecha"));
		facturaList=mongoOperation.find(query, Factura.class);

		return facturaList;
	}

	public void setFacturaList(List<Factura> facturaList) {
		this.facturaList = facturaList;
	}

	public String doEditDetail() {
		(FacesContext.getCurrentInstance().getExternalContext().getFlash()).put("selected", selectedProduct);
		return "detail.xhtml";
	}


	public void borrarSelectedFactura(ActionEvent event){			

		System.out.println("Borrando Factura: "+selectedFactura.getId());

		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");			

		mongoOperation.remove(selectedFactura);		

		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Delete Successfully!"));
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


	public void crearCliente(ActionEvent event){	

		Cliente c = (Cliente)event.getComponent().getAttributes().get("client");

		if(c==null){
			System.out.println("Cliente vacia ");
		}else{
			ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			System.out.println("NuevaFactura NO vacia : "+c.getNif());
			mongoOperation.save(c);
			c = new Cliente();
		}
	}

	public void doDelete(ActionEvent event){
		try {			
			productService.deleteProduct(selectedProduct);

			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Delete Successfully!"));

		}
		catch (DataAccessException e ) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error when deleting product!",null));
		}

	}
	
	private Factura nuevaFactura;
	private String newConcepto;	
	private Cliente selectedClient;
	

	
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
	

	public Factura getNuevaFactura() {
		return nuevaFactura;
	}

	public void setNuevaFactura(Factura nuevaFactura) {
		this.nuevaFactura = nuevaFactura;
	}
	
	
	public void  añadirConceptos(ActionEvent event) {

		//nuevaFactura = (Factura)event.getComponent().getAttributes().get("fact");
		List<String> conceptos = nuevaFactura.getConceptos();
		if (CollectionUtils.isEmpty(conceptos)) {
			nuevaFactura.setConceptos(new ArrayList<String>());
		}
		nuevaFactura.getConceptos().add(this.newConcepto);
		newConcepto="";

	}
	
	public void crearFactura(ActionEvent event){	

		Factura f2 = (Factura)event.getComponent().getAttributes().get("fact");
		Factura f = nuevaFactura;
		if(selectedClient!=null){

			if(f==null){
				System.out.println("NuevaFactura vacia ");
			}else{
				ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
				MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
				System.out.println("NuevaFactura NO vacia : "+f.getNif());
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
