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
public class PgComplete extends PgBase{
	
	protected String workplace;
	protected String address;
	protected String work;
	protected String workDetails;
	protected BigDecimal totalTaf;
	protected BigDecimal totalDu;
	
	protected List<ManipBase> manips = new ArrayList<ManipBase>();
	
	public PgComplete(){
		
	}
	
	public PgComplete(Pg pg, List<PgManip> manips){

		super(pg);
		
		this.workplace = pg.getWorkplace();
		this.address = pg.getAddress();
		this.work = pg.getWork();
		this.workDetails = pg.getWorkDetails();
		
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
	
	/**
	 * Calcul du  total à payer du Pg en prenant en compte le cout des cotizs,
	 * et ses participations aux manips
	 * @return
	 */
	public BigDecimal getTotalDu(){
		BigDecimal result = new BigDecimal(0);
		
		if(manips != null){
			for (ManipBase manip : manips) {
				if(manip != null){
					if(manip.getType() != 3){//On ne prend pas en compte les apports
						BigDecimal current = manip.getTotalPrice();
						if(current != null){
							result = result.add(current);
						}
					}
				}
			}
		}
		return result;
	}


}