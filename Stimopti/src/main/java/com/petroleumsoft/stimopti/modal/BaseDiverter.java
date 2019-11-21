package com.petroleumsoft.stimopti.modal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="base_diverter")
public class BaseDiverter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String bdname;
	private String bdvalue;
	public String getBdname() {
		return bdname;
	}
	public void setBdname(String bdname) {
		this.bdname = bdname;
	}
	public String getBdvalue() {
		return bdvalue;
	}
	public void setBdvalue(String bdvalue) {
		this.bdvalue = bdvalue;
	}
	public ProjectDetails getProjectDetails() {
		return projectDetails;
	}
	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="projectDetails")
	ProjectDetails projectDetails;
	public BaseDiverter(String bdname, String bdvalue, ProjectDetails projectDetails) {
		super();
		this.bdname = bdname;
		this.bdvalue = bdvalue;
		this.projectDetails = projectDetails;
	}
	public BaseDiverter() {
		super();
	}
	

}
