package com.infotaf.restapi.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Représente la table pg
 * @author emmanuel
 *
 */
@Entity
@Table(name="pg")
public class Pg{
	
	@Id
	@SequenceGenerator(name="pg_seq",sequenceName="pg_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pg_seq")
	protected int id;
	protected String nums;
	protected String tbk;
	protected String proms;
	protected String firstName;
	protected String lastName;
	protected String workplace;
	protected String address;
	protected String work;
	protected String workDetails;
	protected String password;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pg")
	protected List<PgManip> pgManips;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pg")
	protected List<Role> roles;	
		
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
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
	public String getWorkplace() {
		return workplace;
	}
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getWorkDetails() {
		return workDetails;
	}
	public void setWorkDetails(String workDetails) {
		this.workDetails = workDetails;
	}
	public List<PgManip> getPgManips() {
		return pgManips;
	}
	public void setPgManips(List<PgManip> pgManips) {
		this.pgManips = pgManips;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}