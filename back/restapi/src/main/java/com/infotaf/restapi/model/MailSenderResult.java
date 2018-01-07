package com.infotaf.restapi.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe représentant le résultat d'un envoi de mail
 * @author emmanuel
 *
 */
public class MailSenderResult extends Serializable{

	protected List<String> addressInError = new ArrayList<String>();
	protected boolean success = true;

	public List<String> getAddressInError() {
		return addressInError;
	}

	public void setAddressInError(List<String> addressInError) {
		this.addressInError = addressInError;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
