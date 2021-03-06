package com.infotaf.restapi.web.viewModel;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.PgManip;
import com.infotaf.restapi.model.Serializable;

/**
 * Informations représentait la participation d'un pg à la manip, sans les liens avec le Pg en question
 * @author emmanuel
 *
 */
public class ManipBase extends Serializable{
	
	private static final Logger logger = LoggerFactory.getLogger(ManipBase.class);
	
	protected int id;
	protected int type;
	protected String nom;
	protected BigDecimal prix;
	protected BigDecimal quantite;
	protected BigDecimal totalPrice;
	
	ManipBase(PgManip pgManip){
		logger.debug("IN - pgManip.id: {}", pgManip.getId());
		Manip manip = pgManip.getManip();
		if(manip != null){
			this.id = manip.getId();
			this.nom = manip.getNom();
			this.prix = manip.getPrix();
			this.type = manip.getType();
		}
		this.quantite = pgManip.getQuantite();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public BigDecimal getPrix() {
		return prix;
	}
	public void setPrix(BigDecimal prix) {
		this.prix = prix;
	}
	public BigDecimal getQuantite() {
		return quantite;
	}
	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}
	/**
	 * Calcul du prix total en fonction de la quantité et du cout unitaire
	 * Prends en compte les cotizs et les apports d'argent :apport = résultat négatif
	 * @return
	 */
	public BigDecimal getTotalPrice(){
		logger.debug("IN - manip.id: {}", this.getId());
		if(this.quantite != null && this.prix != null){
			if(this.type == 1){//Manip normale
				return this.quantite.multiply(this.prix);
			}else if(this.type == 2){//Cotiz
				if(this.quantite.compareTo(new BigDecimal(0)) == 0){
					return this.prix;
				}
				else{
					return new BigDecimal(0);
				}

			}else if(this.type == 3){//Apport d'argent
				return this.quantite.multiply(this.prix).multiply(new BigDecimal(-1));
			}
		}
		return null;
	}
}