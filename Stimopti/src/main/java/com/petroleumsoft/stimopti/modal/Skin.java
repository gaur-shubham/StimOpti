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
@Table(name="skin")
public class Skin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String depth;
	String skin;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "projectDetails")
	ProjectDetails projectDetails;
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
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
	public Skin(String depth, String skin, ProjectDetails projectDetails) {
		super();
		this.depth = depth;
		this.skin = skin;
		this.projectDetails = projectDetails;
	}
	public Skin() {
		super();
	}
	
	
	
	

}
