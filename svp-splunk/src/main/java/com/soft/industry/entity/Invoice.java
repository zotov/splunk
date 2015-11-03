package com.soft.industry.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Invoice {
	public long getId();
	public String getComment();
	public void setComment(String comment);
}
