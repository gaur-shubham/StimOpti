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
@Table(name="test_diverter")
public class TestDiverter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String dname;
	private String dvalue;
	private String dtype;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="projectDetails")
	ProjectDetails projectDetails;
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDvalue() {
		return dvalue;
	}
	public TestDiverter(String dname, String dvalue, String dtype, ProjectDetails projectDetails) {
		super();
		this.dname = dname;
		this.dvalue = dvalue;
		this.dtype = dtype;
		this.projectDetails = projectDetails;
	}
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
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
	public TestDiverter() {
		super();
	}
	

}
