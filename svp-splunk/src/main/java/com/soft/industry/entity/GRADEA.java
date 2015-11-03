package com.soft.industry.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="GRADE_A")
public class GRADEA {

	@Id
	@GeneratedValue
	private long id;
	
	
	private String code;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinTable(
	   name = "GRADE_A_B", 
	   //joinColumns = @JoinColumn(name = "CODE_A",referencedColumnName="CODE"), 
	   //inverseJoinColumns = @JoinColumn(name = "CODE_B",referencedColumnName="CODE")
	   joinColumns = @JoinColumn(name = "ID_A"), 
	   inverseJoinColumns = @JoinColumn(name = "ID_B")
	 )
	GRADEB gradeb;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public GRADEB getGradeb() {
		return gradeb;
	}

	public void setGradeb(GRADEB gradeb) {
		this.gradeb = gradeb;
	}
	
}
