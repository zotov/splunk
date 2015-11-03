package com.soft.industry.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Company {
	public long getId();
	public String getName();
}
