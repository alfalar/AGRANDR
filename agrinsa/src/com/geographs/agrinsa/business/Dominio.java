package com.geographs.agrinsa.business;

public class Dominio {
	private String dominio;
	private int code;
	private String value;

	
	public Dominio() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Dominio(int code, String value) {
		super();
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	
	

}
