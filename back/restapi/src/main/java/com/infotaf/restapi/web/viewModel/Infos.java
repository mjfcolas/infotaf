package com.infotaf.restapi.web.viewModel;

import java.util.Date;

import com.infotaf.restapi.model.Serializable;


/**
 * Informations sur les données affichées (date...)
 * @author emmanuel
 *
 */
public class Infos extends Serializable{
		
	protected Date date;

	public Infos(){
		
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}