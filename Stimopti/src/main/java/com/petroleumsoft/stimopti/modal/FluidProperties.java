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
@Table(name = "fluid_properties")
public class FluidProperties {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fluidName;
	private String fluidValue;
	private String fluidType;
	@JoinColumn(name = "projectDetails")
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	ProjectDetails projectDetails;

	public String getFluidName() {
		return fluidName;
	}

	public void setFluidName(String fluidName) {
		this.fluidName = fluidName;
	}

	public String getFluidValue() {
		return fluidValue;
	}

	public void setFluidValue(String fluidValue) {
		this.fluidValue = fluidValue;
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

	public FluidProperties(String fluidName, String fluidValue, String fluidType, ProjectDetails projectDetails) {
		super();
		this.fluidName = fluidName;
		this.fluidValue = fluidValue;
		this.fluidType = fluidType;
		this.projectDetails = projectDetails;
	}

	public String getFluidType() {
		return fluidType;
	}

	public void setFluidType(String fluidType) {
		this.fluidType = fluidType;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FluidProperties() {
		super();
	}

}
