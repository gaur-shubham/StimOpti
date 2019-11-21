package com.petroleumsoft.stimopti.modal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "formation_damage")
public class FormationDamage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer Id;
	String fdname;
	String fdvalue;
	@JoinColumn(name="projectDetails")
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	ProjectDetails projectDetails;
	public String getFdname() {
		return fdname;
	}
	public void setFdname(String fdname) {
		this.fdname = fdname;
	}
	public String getFdvalue() {
		return fdvalue;
	}
	public void setFdvalue(String fdvalue) {
		this.fdvalue = fdvalue;
	}
	public ProjectDetails getProjectDetails() {
		return projectDetails;
	}
	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}
	public Integer getId() {
		return Id;
	}
	public FormationDamage(String fdname, String fdvalue, ProjectDetails projectDetails) {
		super();
		this.fdname = fdname;
		this.fdvalue = fdvalue;
		this.projectDetails = projectDetails;
	}
	public FormationDamage() {
		super();
	}
	

}
