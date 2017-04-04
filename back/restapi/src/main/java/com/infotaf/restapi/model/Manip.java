package com.infotaf.restapi.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Représenta la table manip
 * @author emmanuel
 *
 */
@Entity
@Table(name="manip")
public class Manip{
	
	@Id
	@SequenceGenerator(name="manip_seq",sequenceName="manip_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="manip_seq")
	protected int id;
	
	protected String nom;
	
	protected int type;
	
	@Column(precision=5, scale=2)
	protected BigDecimal prix;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manip")
	protected List<PgManip> pgManips;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getPrix() {
		return prix;
	}
	public void setPrix(BigDecimal prix) {
		this.prix = prix;
	}
	public List<PgManip> getPgManips() {
		return pgManips;
	}
	public void setPgManips(List<PgManip> pgManips) {
		this.pgManips = pgManips;
	}

}