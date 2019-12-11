package com.petroleumsoft.stimopti.modal;

import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Waseem
 *
 */
@Entity
@Table(name = "project_details")
public class ProjectDetails {
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Integer id;
	private String projectName;
	private String wellName;
	private String companyName;
	private String module;
	private Timestamp dateCreated;
	/* One to many Mapping for deletingByProjectDetails */
	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<WellData> welldata;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<WellCompletion> wellCompletion;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<WellCompletionPerf> wellCompletionperf;
	
	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<Penetration> penetration;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<Placement> placement;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<Skin> skin;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<Productivity> productivity;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<PumpingEquipment> pumpingEquipment;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<AcidProperties> acidProperties;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<BaseDiverter> baseDiverter;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<FluidProperties> fluidProperties;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<Coreflood> coreflood;
	
	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<ReservoirLithology> reservoir;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<PotentialHazard> hazard;

	@OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
	private Collection<WellDataCompletionType> wdCompType;
	/* End of the Block */

	/* Getters Setters for deleteByProjectDetails */
	
	public Collection<Coreflood> getCoreflood() {
		return coreflood;
	}

	public void setCoreflood(Collection<Coreflood> coreflood) {
		this.coreflood = coreflood;
	}

	public Collection<Penetration> getPenetration() {
		return penetration;
	}

	public void setPenetration(Collection<Penetration> penetration) {
		this.penetration = penetration;
	}

	public Collection<Placement> getPlacement() {
		return placement;
	}

	public void setPlacement(Collection<Placement> placement) {
		this.placement = placement;
	}

	public Collection<Skin> getSkin() {
		return skin;
	}

	public void setSkin(Collection<Skin> skin) {
		this.skin = skin;
	}

	public Collection<Productivity> getProductivity() {
		return productivity;
	}

	public void setProductivity(Collection<Productivity> productivity) {
		this.productivity = productivity;
	}

	public Collection<WellCompletion> getWellCompletion() {
		return wellCompletion;
	}

	public void setWellCompletion(Collection<WellCompletion> wellCompletion) {
		this.wellCompletion = wellCompletion;
	}

	public Collection<FluidProperties> getFluidProperties() {
		return fluidProperties;
	}

	public void setFluidProperties(Collection<FluidProperties> fluidProperties) {
		this.fluidProperties = fluidProperties;
	}

	public Collection<AcidProperties> getAcidProperties() {
		return acidProperties;
	}

	public void setAcidProperties(Collection<AcidProperties> acidProperties) {
		this.acidProperties = acidProperties;
	}

	public Collection<PumpingEquipment> getPumpingEquipment() {
		return pumpingEquipment;
	}

	public void setPumpingEquipment(Collection<PumpingEquipment> pumpingEquipment) {
		this.pumpingEquipment = pumpingEquipment;
	}

	public Collection<WellData> getWelldata() {
		return welldata;
	}

	public void setWelldata(Collection<WellData> welldata) {
		this.welldata = welldata;
	}

	public Collection<BaseDiverter> getBaseDiverter() {
		return baseDiverter;
	}

	public void setBaseDiverter(Collection<BaseDiverter> baseDiverter) {
		this.baseDiverter = baseDiverter;
	}
	
	public Collection<ReservoirLithology> getReservoir() {
		return reservoir;
	}

	public void setReservoir(Collection<ReservoirLithology> reservoir) {
		this.reservoir = reservoir;
	}

	public Collection<PotentialHazard> getHazard() {
		return hazard;
	}

	public void setHazard(Collection<PotentialHazard> hazard) {
		this.hazard = hazard;
	}

	/* End of the getter and setter block */
	public ProjectDetails(String projectName, String wellName, String companyName, String module,
			Timestamp dateCreated) {
		super();
		this.projectName = projectName;
		this.wellName = wellName;
		this.companyName = companyName;
		this.module = module;
		this.dateCreated = dateCreated;
	}

	public ProjectDetails() {
		// TODO Auto-generated constructor stub
	}

}
