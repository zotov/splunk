package com.soft.industry.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

//@Embeddable
public class DEId implements Serializable {
	
	/**	 **/
	private static final long serialVersionUID = -5374511669211483254L;
	/*
	@Column(name="ID")
	private long id;*/
	@Column(name="COUNTRY_ID")
	private long country_id;
	@Column(name="DEPARTMENT_ID")
	private long department_id;
	
	/*@OneToOne
    @JoinColumn(name="COUNTRY_ID")
	private Country country;*/
	
	  
	
	public long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(long country_id) {
		this.country_id = country_id;
	}

	public long getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}	

	/*public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}*/

	/*public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
*/
	public String toString() {
		return " country_id:" + this.country_id + ", department_id:" + this.department_id;
	}

}
