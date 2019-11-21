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
@Table(name="placement")
public class Placement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String depth;
	String placement;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "projectDetails")
	ProjectDetails projectDetails;
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getPlacement() {
		return placement;
	}
	public void setPlacement(String placement) {
		this.placement = placement;
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
	public Placement(String depth, String placement, ProjectDetails projectDetails) {
		super();
		this.depth = depth;
		this.placement = placement;
		this.projectDetails = projectDetails;
	}
	public Placement() {
		super();
	}
	
	
	
	

}
