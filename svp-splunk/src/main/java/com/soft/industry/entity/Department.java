package com.soft.industry.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="DEPARTMENT")
public class Department implements Company{
	
	@Id
	@GeneratedValue
	private long id;
	
	private String name;	
	
	/*@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(
	   name = "EDC", 
	   joinColumns = @JoinColumn(name = "ID_DEPARTMENT"), 
	   inverseJoinColumns = @JoinColumn(name = "ID_EMPLOYEE")
	 )	*/
	@Transient
	Set<Employee> employee = new HashSet<Employee>();
		
	public Set<Employee> getEmployee() {
		return employee;
	}

	public void setEmployee(Set<Employee> employee) {
		this.employee = employee;
	}
	
	public void addEmployee(Employee employee) {
		this.employee.add(employee);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String toString() {
		return "id:" + id + 
				", name:" + name + 
				//", contry:" + this.country+ 
				", employee:" + Arrays.toString(employee.toArray());
				//", de:" + Arrays.toString(de.toArray());
	}

}
