package com.soft.industry.entity;

import java.util.Arrays;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@Entity
//@Table(name="EDC")
public class DE  {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Embedded
	DEId deId;
		
	private String name;
	
	@OneToMany(fetch=FetchType.EAGER)
	 @JoinTable(
	   name = "EDC", 
	   joinColumns = {@JoinColumn(name = "ID_COUNTRY"),@JoinColumn(name = "ID_DEPARTMENT")}, 
	   inverseJoinColumns = @JoinColumn(name = "ID_EMPLOYEE")
	 )
	 Set<Employee> employees;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id")
	private Department department;
	
	public DEId getDeId() {
		return deId;
	}


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}


	public void setDeId(DEId deId) {
		this.deId = deId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Set<Employee> getEmployees() {
		return employees;
	}


	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}


	public String toString() {
		return "deId:" + deId +
				", name:" + name +
				", employees:" + Arrays.toString(employees.toArray());
	}

}
