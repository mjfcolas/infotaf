package com.infotaf.restapi.web.viewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;

/**
 * Représénte un Pg avec la liste de toutes les manips auxquelles il a participé
 * @author emmanuel
 *
 */
public class PgComplete{
	
	protected int id;
	protected String nums;
	protected String tbk;
	protected String proms;
	protected String firstName;
	protected String lastName;
	protected BigDecimal totalTaf;
	
	protected List<ManipBase> manips = new ArrayList<ManipBase>();
	
	public PgComplete(){
		
	}
	
	public PgComplete(Pg pg, List<PgManip> manips){
		this.id = pg.getId();
		this.nums = pg.getNums();
		this.tbk = pg.getTbk();
		this.proms = pg.getProms();
		this.firstName = pg.getFirstName();
		this.lastName = pg.getLastName();
		
		if(manips != null){
			for (PgManip pgManip : manips) {
				boolean isCotizOrNotZero = pgManip.getManip().getType() == 2 
						|| pgManip.getQuantite().compareTo(new BigDecimal(0)) != 0; 
				if(pgManip != null && isCotizOrNotZero){
					this.manips.add(new ManipBase(pgManip));
				}
			}
		}
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

	public List<ManipBase> getManips() {
		return manips;
	}

	public void setManips(List<ManipBase> manips) {
		this.manips = manips;
	}
	/**
	 * Calcul du TAF total du Pg en prenant en compte le cout des cotizs,
	 * ses apport d'argent et ses participations aux manips
	 * Positif: Le Pg doit de l'argent
	 * Négatif: L'argent est dûe au Pg
	 * @return
	 */
	public BigDecimal getTotalTaf(){
		BigDecimal result = new BigDecimal(0);
		
		if(manips != null){
			for (ManipBase manip : manips) {
				if(manip != null){
					BigDecimal current = manip.getTotalPrice();
					if(current != null){
						result = result.add(current);
					}
				}
			}
		}
		return result;
	}


}