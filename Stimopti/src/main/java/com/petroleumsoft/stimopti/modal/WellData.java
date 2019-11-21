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
@Table(name = "well_data")
public class WellData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer Id;
	String wp;
	String wv;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
	@JoinColumn(name = "project_id")
	ProjectDetails projectDetails;

	public String getWp() {
		return wp;
	}

	public void setWp(String wp) {
		this.wp = wp;
	}

	public String getWv() {
		return wv;
	}

	public void setWv(String wv) {
		this.wv = wv;
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

	public WellData(String wp, String wv, ProjectDetails projectDetails) {
		super();
		this.wp = wp;
		this.wv = wv;
		this.projectDetails = projectDetails;
	}

	public WellData() {
		super();
	}

}
