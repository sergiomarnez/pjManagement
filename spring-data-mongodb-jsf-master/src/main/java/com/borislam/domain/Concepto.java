package com.borislam.domain;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Concepto implements Serializable{

	private String concepto;
	private Double importe;
	
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	
	public Concepto(){
		
	}
	public Concepto(String concepto, Double importe) {
		super();
		this.concepto = concepto;
		this.importe = importe;
	}
	
	
}
