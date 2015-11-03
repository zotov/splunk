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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soft.industry.server.StandaloneServer;

@Entity
@Table(name="EDC")
public class EDC  {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(EDC.class);
	
	@Id
	@GeneratedValue
	private long id;
		
	@OneToOne
    @JoinColumn(name="ID_COUNTRY")
	Country country;
	
	@OneToOne
	@JoinColumn(name="ID_DEPARTMENT")
	Department department;
	
	@OneToOne
	@JoinColumn(name="ID_EMPLOYEE")
	Employee employee;

	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Country getCountry() {
		return country;
	}


	public void setCountry(Country country) {
		this.country = country;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	

	public String toString() {		
		return LogStr.getInstance().
				append("id",String.valueOf(id)).
				append("country",country.getName()).
				append("department",department.getName()).
				append("employee",employee.getName()).toString();
	}

}
