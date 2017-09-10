package com.infotaf.restapi.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.infotaf.restapi.security.model.RoleEnum;

/**
 * Représente la table role
 * @author emmanuel
 *
 */
@Entity
@Table(name="role")
public class Role{
	
	@Id
	@SequenceGenerator(name="role_seq",sequenceName="role_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="role_seq")
	protected int id;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pg")
    private Pg pg;
	private String role;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Pg getPg() {
		return pg;
	}
	public void setPg(Pg pg) {
		this.pg = pg;
	}
	public RoleEnum getRole() {
		return RoleEnum.valueOf(this.role);	
	}
	public void setRole(RoleEnum role) {
		this.role = role.name();
	}
	
	
}