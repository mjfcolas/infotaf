package com.infotaf.restapi.model;

/**
 * Représente le résultat d'une opération sur le serveur
 * @author emmanuel
 *
 */
public class BusinessStatus {

	protected boolean success = true;
	protected String message = "";
	protected String value = "";
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	} 
	
	
}
