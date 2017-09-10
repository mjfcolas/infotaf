package com.infotaf.restapi.model;

import com.infotaf.restapi.web.viewModel.Serializable;

/**
 * Représente le résultat d'une opération sur le serveur
 * @author emmanuel
 *
 */
public class BusinessStatus {

	protected boolean success = true;
	protected String message = "";
	protected String value = "";
	protected Serializable object;
	
	public Serializable getObject() {
		return object;
	}
	public void setObject(Serializable object) {
		this.object = object;
	}
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
