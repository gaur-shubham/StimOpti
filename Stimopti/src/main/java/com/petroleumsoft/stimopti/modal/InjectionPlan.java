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
@Table(name = "injection_plan")
public class InjectionPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	String duration;
	String stage;
	String stavageVolBbls;
	String direction;
	String pumpRateBMP;
	String injPressPSI;
	String bhppsi;
	String injDefthFT;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "projectDetails")
	ProjectDetails projectDetails;
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getStavageVolBbls() {
		return stavageVolBbls;
	}
	public void setStavageVolBbls(String stavageVolBbls) {
		this.stavageVolBbls = stavageVolBbls;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getPumpRateBMP() {
		return pumpRateBMP;
	}
	public void setPumpRateBMP(String pumpRateBMP) {
		this.pumpRateBMP = pumpRateBMP;
	}
	public String getInjPressPSI() {
		return injPressPSI;
	}
	public void setInjPressPSI(String injPressPSI) {
		this.injPressPSI = injPressPSI;
	}
	public String getBhppsi() {
		return bhppsi;
	}
	public void setBhppsi(String bhppsi) {
		this.bhppsi = bhppsi;
	}
	public String getInjDefthFT() {
		return injDefthFT;
	}
	public void setInjDefthFT(String injDefthFT) {
		this.injDefthFT = injDefthFT;
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
	public InjectionPlan(String duration, String stage, String stavageVolBbls, String direction, String pumpRateBMP,
			String injPressPSI, String bhppsi, String injDefthFT, ProjectDetails projectDetails) {
		super();
		this.duration = duration;
		this.stage = stage;
		this.stavageVolBbls = stavageVolBbls;
		this.direction = direction;
		this.pumpRateBMP = pumpRateBMP;
		this.injPressPSI = injPressPSI;
		this.bhppsi = bhppsi;
		this.injDefthFT = injDefthFT;
		this.projectDetails = projectDetails;
	}
	public InjectionPlan() {
		super();
	}
	

}
