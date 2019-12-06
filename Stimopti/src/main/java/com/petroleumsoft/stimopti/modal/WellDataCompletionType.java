/**
 * 
 */
package com.petroleumsoft.stimopti.modal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author ShubhamGaur
 *
 */
@Entity
@Table(name = "WelldataComptype")
public class WellDataCompletionType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String cn;
	private String cv;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="projectDetails")
	ProjectDetails projectDetails;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
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
	public WellDataCompletionType(String cn, String cv, ProjectDetails projectDetails) {
		super();
		this.cn = cn;
		this.cv = cv;
		this.projectDetails = projectDetails;
	}
	public WellDataCompletionType() {
		super();
	}
	
}
