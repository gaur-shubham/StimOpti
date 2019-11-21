package com.petroleumsoft.stimopti.modal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reservoir_lithology")
public class ReservoirLithology {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "fromDefthFT")
	private String fromDefthFT;
	@Column(name = "toDefthFT")
	private String toDefthFT;
	@Column(name = "tVDFT")
	private String tVDFT;
	@Column(name = "permMD")
	private String permMD;
	@Column(name = "poro")
	private String poro;
	@Column(name = "zonePressPSI")
	private String zonePressPSI;
	@Column(name = "prestimskin")
	private String prestimskin;
	@Column(name = "prestimpi")
	private String prestimpi;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "projectDetails")
	private ProjectDetails projectDetails;

	public String getFrommDefthFT() {
		return fromDefthFT;
	}

	public void setFromDefthFT(String fromDefthFT) {
		this.fromDefthFT = fromDefthFT;
	}

	public String getToDefthFT() {
		return toDefthFT;
	}

	public void setToDefthFT(String toDefthFT) {
		this.toDefthFT = toDefthFT;
	}

	public String gettVDFT() {
		return tVDFT;
	}

	public void settVDFT(String tVDFT) {
		this.tVDFT = tVDFT;
	}

	public String getPermMD() {
		return permMD;
	}

	public void setPermMD(String permMD) {
		this.permMD = permMD;
	}

	public String getPoro() {
		return poro;
	}

	public void setPoro(String poro) {
		this.poro = poro;
	}

	public String getZonePressPSI() {
		return zonePressPSI;
	}

	public void setZonePressPSI(String zonePressPSI) {
		this.zonePressPSI = zonePressPSI;
	}

	public String getPrestimskin() {
		return prestimskin;
	}

	public void setPrestimskin(String prestimskin) {
		this.prestimskin = prestimskin;
	}

	public String getPrestimpi() {
		return prestimpi;
	}

	public void setPrestimpi(String prestimpi) {
		this.prestimpi = prestimpi;
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

	public ReservoirLithology(String fromDefthFT, String toDefthFT, String tVDFT, String permMD, String poro,
			String zonePressPSI, String prestimskin, String prestimpi, ProjectDetails projectDetails) {
		super();
		this.fromDefthFT = fromDefthFT;
		this.toDefthFT = toDefthFT;
		this.tVDFT = tVDFT;
		this.permMD = permMD;
		this.poro = poro;
		this.zonePressPSI = zonePressPSI;
		this.prestimskin = prestimskin;
		this.prestimpi = prestimpi;
		this.projectDetails = projectDetails;
	}

	public ReservoirLithology() {
		// TODO Auto-generated constructor stub
	}
}
