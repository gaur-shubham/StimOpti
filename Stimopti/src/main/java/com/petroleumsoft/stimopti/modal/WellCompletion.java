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
@Table(name = "well_completion")
public class WellCompletion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String cp;
	String cv;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
	@JoinColumn(name = "project_id")
	ProjectDetails projectDetails;
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getCv() {
		return cv;
	}
	public void setCv(String cv) {
		this.cv = cv;
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
	public WellCompletion(String cp, String cv, ProjectDetails projectDetails) {
		super();
		this.cp = cp;
		this.cv = cv;
		this.projectDetails = projectDetails;
	}
	public WellCompletion() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
