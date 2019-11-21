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
@Table(name = "penetration")
public class Penetration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String depth;
	String penetration;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "projectDetails")
	ProjectDetails projectDetails;
	
	public Penetration() {
		super();
	}

	public Penetration(String depth, String penetration, ProjectDetails projectDetails) {
		super();
		this.depth = depth;
		this.penetration = penetration;
		this.projectDetails = projectDetails;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getPenetration() {
		return penetration;
	}

	public void setPenetration(String penetration) {
		this.penetration = penetration;
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


}