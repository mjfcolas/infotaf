package com.infotaf.restapi.web.viewModel;

import com.infotaf.restapi.model.Pg;

/**
 * Représénte un Pg avec la liste de toutes les manips auxquelles il a participé
 * @author emmanuel
 *
 */
public class PgBase extends Serializable{
	
	protected int id;
	protected String nums;
	protected String tbk;
	protected String proms;
	protected String firstName;
	protected String lastName;
	
	
	public PgBase(){
		
	}
	
	public PgBase(Pg pg){
		this.id = pg.getId();
		this.nums = pg.getNums();
		this.tbk = pg.getTbk();
		this.proms = pg.getProms();
		this.firstName = pg.getFirstName();
		this.lastName = pg.getLastName();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNums() {
		return nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}

	public String getTbk() {
		return tbk;
	}

	public void setTbk(String tbk) {
		this.tbk = tbk;
	}

	public String getProms() {
		return proms;
	}

	public void setProms(String proms) {
		this.proms = proms;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}