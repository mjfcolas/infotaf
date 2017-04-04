package com.infotaf.restapi.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Représente la table pg_manip
 * @author emmanuel
 *
 */
@Entity
@Table(name="pg_manip")
public class PgManip{
	
	@Id
	@SequenceGenerator(name="pg_manip_seq",sequenceName="pg_manip_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pg_manip_seq")
	protected int id;
	
	@Column(precision=5, scale=2)
	protected BigDecimal quantite;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pg")
    private Pg pg;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_manip")
    private Manip manip;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getQuantite() {
		return quantite;
	}
	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}
	public Pg getPg() {
		return pg;
	}
	public void setPg(Pg pg) {
		this.pg = pg;
	}
	public Manip getManip() {
		return manip;
	}
	public void setManip(Manip manip) {
		this.manip = manip;
	}
 
	
}