package com.soft.industry.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.soft.industry.company.CountryListener;

@Entity
@Table(name = "COUNTRY")
// @EntityListeners({CountryListener.class})
public class Country implements Company {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_COUNTRY")
	private Set<EDC> edcList;

	//@OneToMany(fetch = FetchType.EAGER)
	//@JoinTable(name = "EDC", joinColumns = @JoinColumn(name = "ID_COUNTRY"), inverseJoinColumns = @JoinColumn(name = "ID_DEPARTMENT"))
	 @Transient
	List<Department> departments = new ArrayList<Department>();

	public Set<EDC> getEdcList() {
		return edcList;
	}

	public void setEdcList(Set<EDC> edcList) {
		this.edcList = edcList;
	}

	// @PostLoad
	public void buildDepartments() {
		HashMap<Long,Department> de = new HashMap<Long,Department>();
			for (EDC edc : edcList) {
				long id = edc.getDepartment().getId();
				Department department;
					if (!de.containsKey(edc.getDepartment().getId())) {
						Department edcD = edc.getDepartment();
						 department = new Department();
						department.setId(edcD.getId());
						department.setName(edcD.getName());
						department.addEmployee(edc.getEmployee().clone());
						de.put(id, department);
						continue;
					}
					department = de.get(id);
					department.addEmployee(edc.getEmployee().clone());
           }
			
		   this.setDepartments(new ArrayList<Department>(de.values()));
	}

	@PostLoad
	public List<Department> getDepartments() {
		return departments;
	}

	@PostLoad
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public long getId() {
		return id;
	}

	@PostLoad
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
		return "id:" + id + ", name:" + name + ", departments:"
				+ Arrays.toString(this.departments.toArray()) ;//+
				//", edc:"+ Arrays.toString(this.edcList.toArray());
	}

}
