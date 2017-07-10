package com.infotaf.restapi.model;

/**
 * Représente le résultat d'une opération sur le serveur
 * @author emmanuel
 *
 */
public class BusinessStatus {

	protected boolean success = true;
	protected String message = "";
	
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
	
	
}
