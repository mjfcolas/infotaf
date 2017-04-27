package com.infotaf.restapi.web.viewModel;

import java.util.Date;


/**
 * Informations sur les données affichées (date...)
 * @author emmanuel
 *
 */
public class Infos{
		
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