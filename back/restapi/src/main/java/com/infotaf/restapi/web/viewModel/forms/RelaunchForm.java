package com.infotaf.restapi.web.viewModel.forms;



import com.infotaf.restapi.model.Serializable;


/**
 * Représentation du formulaire de relance du menu administration
 * @author emmanuel
 *
 */
public class RelaunchForm extends Serializable{
		
	protected Integer amount;
	protected boolean isSimulation;

	public RelaunchForm(){
		
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public boolean getIsSimulation() {
		return isSimulation;
	}

	public void setIsSimulation(boolean isSimulation) {
		this.isSimulation = isSimulation;
	}
	
}