package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petroleumsoft.stimopti.algos.Bullhead;
import com.petroleumsoft.stimopti.algos.CoiledTubing;
import com.petroleumsoft.stimopti.modal.AcidProperties;
import com.petroleumsoft.stimopti.modal.BaseDiverter;
import com.petroleumsoft.stimopti.modal.FluidProperties;
import com.petroleumsoft.stimopti.modal.FormationDamage;
import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.Placement;
import com.petroleumsoft.stimopti.modal.Productivity;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.PumpingEquipment;
import com.petroleumsoft.stimopti.modal.ReservoirLithology;
import com.petroleumsoft.stimopti.modal.Skin;
import com.petroleumsoft.stimopti.modal.WellCompletion;
import com.petroleumsoft.stimopti.modal.WellData;
import com.petroleumsoft.stimopti.repo.AcidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.BaseDiverterRepo;
import com.petroleumsoft.stimopti.repo.FluidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.FormationDamageRepo;
import com.petroleumsoft.stimopti.repo.InjectionPlanRepo;
import com.petroleumsoft.stimopti.repo.PenetrationRepo;
import com.petroleumsoft.stimopti.repo.PlacementRepo;
import com.petroleumsoft.stimopti.repo.ProductivityRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.PumpingEquipmentRepo;
import com.petroleumsoft.stimopti.repo.ReservoirLithologyRepo;
import com.petroleumsoft.stimopti.repo.SkinRepo;
import com.petroleumsoft.stimopti.repo.WellCompletionRepo;
import com.petroleumsoft.stimopti.repo.WellDataRepo;
import com.petroleumsoft.stimopti.services.GenerateInputService;

@Service("generateInputService")
public class GenerateInputServiceImplementation implements GenerateInputService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepo;
	@Autowired
	WellDataRepo wellDataRepo;
	@Autowired
	PumpingEquipmentRepo pumpingEquipmentRepo;
	@Autowired
	FluidPropertiesRepo fluidPropertiesRepo;
	@Autowired
	BaseDiverterRepo baseDiverterRepo;
	@Autowired
	FormationDamageRepo formationDamageRepo;
	@Autowired
	InjectionPlanRepo injectionPlanRepo;
	@Autowired
	ReservoirLithologyRepo reservoirLithologyRepo;
	@Autowired
	WellCompletionRepo wellCompletionRepo;
	@Autowired
	AcidPropertiesRepo acidPropertiesRepo;
	@Autowired
	PenetrationRepo penetrationRepo;
	@Autowired
	PlacementRepo placementRepo;
	@Autowired
	SkinRepo skinRepo;
	@Autowired
	ProductivityRepo productivityRepo;

	@Override
	public void generateInput(Integer id) {
		ProjectDetails details = projectDetailsRepo.findById(id).orElse(null);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("src/main/resources/config/Acid_Carbonate_Input.txt"));
			writer.write("/**** Acid Carbonate Input File ****/" + "\n");
			writer.write("Project Name=" + details.getProjectName() + "\n");
			writer.write("Project Name=" + details.getCompanyName() + "\n");
			writer.write("Project Name=" + details.getWellName() + "\n");

			writer.write("/*** Well Properties ***/" + "\n");
			List<WellData> wellData = wellDataRepo.findByProjectDetails(details);
			String wellType = null;
			for (WellData well : wellData) {
				if (well.getWp().equals("Well Type")) {
					wellType = well.getWv();
				}
				writer.write(well.getWp().toUpperCase() + "=" + well.getWv() + "\n");

			}
			writer.write("/*** Well Completion ***/" + "\n");
			List<WellCompletion> wellCompletion = wellCompletionRepo.findByProjectDetails(details);
			for (WellCompletion comp : wellCompletion) {
				writer.write(comp.getCp().toUpperCase() + "=" + comp.getCv() + "\n");
			}

			writer.write("perfSTARTFT" + "=" + "3325.0" + "\n");
			writer.write("perfENDFT" + "=" + "3365.0" + "\n");
			writer.write("COREFLOOD" + "=" + "NOT AVAILABLE" + "\n");
			writer.write("INJECTION FLUID SPECIFIC GRAVITY" + "=" + "0.5" + "\n");
			writer.write("ACID TYPE" + "=" + "N/A EMULSIFIED" + "\n");

			writer.write("/*** Pumping Methodologies ***/" + "\n");
			String pumpingType = null;
			List<PumpingEquipment> pumpingEquipment = pumpingEquipmentRepo.findByProjectDetails(details);
			for (PumpingEquipment pump : pumpingEquipment) {
				if (pump.getPp().equals("Injection Type")) {
					pumpingType = pump.getPv();
				}
				writer.write(pump.getPp().toUpperCase() + "=" + pump.getPv() + "\n");

			}
			writer.write("/*** Fluid Properties ***/" + "\n");
			List<FluidProperties> fluidProperties = fluidPropertiesRepo.findByProjectDetails(details);
			for (FluidProperties fluid : fluidProperties) {
				writer.write(fluid.getFluidName().toUpperCase() + "=" + fluid.getFluidValue() + "\n");
			}
			writer.write("/*** Base Diverter ***/" + "\n");
			List<BaseDiverter> baseDiverter = baseDiverterRepo.findByProjectDetails(details);
			for (BaseDiverter bd : baseDiverter) {
				writer.write(bd.getBdname().toUpperCase() + "=" + bd.getBdvalue() + "\n");

			}
			writer.write("/*** Formation Damage ***/" + "\n");
			List<FormationDamage> formationDamage = formationDamageRepo.findByprojectDetails(details);
			for (FormationDamage fd : formationDamage) {
				writer.write(fd.getFdname().toUpperCase() + "=" + fd.getFdvalue() + "\n");
			}

			writer.write("/*** Acid Properties  ***/" + "\n");
			List<AcidProperties> acidProperties = acidPropertiesRepo.findByProjectDetails(details);
			for (AcidProperties fd : acidProperties) {
				if (fd.getAn().contains("Concentration")) {
					writer.write("Concentration (%)" + "=" + fd.getAv() + "\n");

				}
				if (fd.getAn().contains("Viscosity")) {
					writer.write("Viscosity (cP)" + "=" + fd.getAv() + "\n");

				}
				if (fd.getAn().contains("Specific Gravity")) {
					writer.write("Specific Gravity" + "=" + fd.getAv() + "\n");

				}
				if (fd.getAn().contains("Diffusion Co-Efficient (Ft2/Min)")) {
					writer.write("Diffusion Co-Efficient (Ft2/Min)" + "=" + fd.getAv() + "\n");

				}

			}
			writer.write("/*** Injection Plan ***/" + "\n");
			List<InjectionPlan> injectionPlan = injectionPlanRepo.findByprojectDetails(details);
			int i = 0;
			for (InjectionPlan ip : injectionPlan) {
				writer.write("Duration" + i + "=" + ip.getDuration() + "\n");
				writer.write("Stage" + i + "=" + ip.getStage() + "\n");
				writer.write("Pumprate" + i + "=" + ip.getPumpRateBMP() + "\n");
				writer.write("Volume" + i + "=" + ip.getStavageVolBbls() + "\n");
				writer.write("Bhp" + i + "=" + ip.getBhppsi() + "\n");
				writer.write("Injp" + i + "=" + ip.getInjPressPSI() + "\n");
				writer.write("Direction" + i + "=" + ip.getDirection() + "\n");
				writer.write("InjDepth" + i + "=" + ip.getInjDefthFT() + "\n");
				i++;
			}
			writer.write("Stages" + "=" + i + "\n");

			i = 0;
			writer.write("/*** Reservoir Lithology ***/" + "\n");
			List<ReservoirLithology> reservoirLithology = reservoirLithologyRepo.findByprojectDetails(details);
			double newto = 0.0;
			for (ReservoirLithology rl : reservoirLithology) {

				double from = Double.parseDouble(rl.getFrommDefthFT());
				double to = Double.parseDouble(rl.getToDefthFT());
				double tvd = Double.parseDouble(rl.gettVDFT());
				double poro = Double.parseDouble(rl.getPoro());
				double perm = Double.parseDouble(rl.getPermMD());
				double zonal = Double.parseDouble(rl.getZonePressPSI());
				double preskin = Double.parseDouble(rl.getPrestimskin());
				double prepi = Double.parseDouble(rl.getPrestimpi());
				newto = to;

				do {
					if (wellType.equals("Vertical")) {
						tvd = from;
					}
					writer.write("FromDepth" + i + "=" + from + "\n");
					from += 1;
					writer.write("ToDepth" + i + "=" + from + "\n");
					writer.write("Tvd" + i + "=" + tvd + "\n");
					writer.write("Poro" + i + "=" + poro + "\n");
					writer.write("Perm" + i + "=" + perm + "\n");
					writer.write("ZonalPressure" + i + "=" + zonal + "\n");
					writer.write("Pre-Skin" + i + "=" + preskin + "\n");
					writer.write("Pre-Pi" + i + "=" + prepi + "\n");
					i++;

				} while (from != (newto - 1));

			}
			writer.write("Layers" + "=" + i + "\n");

			writer.write("/*** End of Input File ***/" + "\n");
			writer.close();

			/* Calling Output Classes */
			generateOuput(pumpingType, details);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void generateOuput(String pumpingType, ProjectDetails details) {
		try {

			Map<String, String> penetLengMap = new LinkedHashMap<String, String>();
			Map<String, String> volumepercentage = new LinkedHashMap<String, String>();
			Map<String, String> skincalculationplot = new LinkedHashMap<String, String>();
			Map<String, String> productivityindex = new LinkedHashMap<String, String>();
			Map<String, String> foldmap = new LinkedHashMap<String, String>();

			if (pumpingType.equals("Bullhead")) {
				Bullhead bh = new Bullhead();
				bh.bullhead(penetLengMap, volumepercentage, productivityindex, skincalculationplot);
			}
			if (pumpingType.equals("Ct")) {
				CoiledTubing ct = new CoiledTubing();
				ct.calculatecarbonateCT(penetLengMap, volumepercentage, productivityindex, skincalculationplot,foldmap);
			}
			List<Penetration> penetrationList = new ArrayList<Penetration>();
			for (Map.Entry<String, String> entry : penetLengMap.entrySet()) {
				Penetration penetration = new Penetration();
				penetration.setDepth(entry.getKey());
				penetration.setPenetration(entry.getValue());
				penetration.setProjectDetails(details);
				penetrationList.add(penetration);
			}
			System.out.println("Penetration : " + penetrationList);
			penetrationRepo.saveAll(penetrationList);

			List<Placement> placementList = new ArrayList<Placement>();
			for (Map.Entry<String, String> entry : volumepercentage.entrySet()) {
				Placement placement = new Placement();
				placement.setDepth(entry.getKey());
				placement.setPlacement(entry.getValue());
				placement.setProjectDetails(details);
				placementList.add(placement);
			}
			placementRepo.saveAll(placementList);

			List<Skin> skinList = new ArrayList<Skin>();
			for (Map.Entry<String, String> entry : skincalculationplot.entrySet()) {
				Skin skin = new Skin();
				skin.setDepth(entry.getKey());
				skin.setSkin(entry.getValue());
				skin.setProjectDetails(details);
				skinList.add(skin);
			}
			skinRepo.saveAll(skinList);

			List<Productivity> productivityList = new ArrayList<Productivity>();
			for (Map.Entry<String, String> entry : productivityindex.entrySet()) {
				Productivity productivity = new Productivity();
				productivity.setDepth(entry.getKey());
				productivity.setProductivity(entry.getValue());
				productivity.setProjectDetails(details);
				productivityList.add(productivity);
			}
			productivityRepo.saveAll(productivityList);

			System.out.println("Data Saved");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void fillData() {
		// TODO Auto-generated method stub

	}

}
