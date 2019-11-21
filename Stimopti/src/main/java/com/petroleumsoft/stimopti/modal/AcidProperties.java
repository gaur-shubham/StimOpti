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
@Table(name="acid_properties")
public class AcidProperties {
@GeneratedValue(strategy =GenerationType.IDENTITY)	
@Id
private Integer id;
private String an;
private String av;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name = "projectDetails")
private ProjectDetails projectDetails;
public String getAn() {
	return an;
}
public void setAn(String an) {
	this.an = an;
}
public String getAv() {
	return av;
}
public void setAv(String av) {
	this.av = av;
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
public AcidProperties(String an, String av, ProjectDetails projectDetails) {
	super();
	this.an = an;
	this.av = av;
	this.projectDetails = projectDetails;
}
public AcidProperties() {
	super();
}

}
