package com.infotaf.restapi.model;

/**
 * Classe représentant un mail à envoyer
 * @author emmanuel
 *
 */
public class Mail {

	protected String object;
	protected String address;
	protected String body;
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
