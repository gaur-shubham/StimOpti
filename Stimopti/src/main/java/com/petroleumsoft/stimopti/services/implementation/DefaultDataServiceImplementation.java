package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.petroleumsoft.stimopti.modal.AcidProperties;
import com.petroleumsoft.stimopti.modal.BaseDiverter;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.TestDiverter;
import com.petroleumsoft.stimopti.repo.AcidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.BaseDiverterRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.TestDiverterRepo;
import com.petroleumsoft.stimopti.services.DefaultDataService;

@Service("defaultDataService")
public class DefaultDataServiceImplementation implements DefaultDataService {
	@Autowired
	AcidPropertiesRepo acidPropertiesRepo;
	@Autowired
	BaseDiverterRepo baseDiverterRepo;
	@Autowired
	TestDiverterRepo testDiverterRepo;
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	ServletContext servletcontext;

	@Override
	public void setDefault(Integer id) throws Exception {
		ProjectDetails projectDetails = projectDetailsRepository.findById(id).orElse(null);
		AcidProperties acidProperties;
		TestDiverter testDiverter;
		BufferedReader br = null;
		String line = "";
		File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
		br = new BufferedReader(new FileReader(file));
		List<AcidProperties> fluiddblist = new ArrayList<AcidProperties>();
		List<BaseDiverter> bdlist = new ArrayList<BaseDiverter>();
		List<TestDiverter> dlist = new ArrayList<TestDiverter>();
		while ((line = br.readLine()) != null) {
			if (line.startsWith("Hcl 5")) {
				while ((line = br.readLine()) != null) {
					if (line.startsWith("/")) {
						break;
					}
					acidProperties = new AcidProperties();
					String[] data = line.split("=");
					acidProperties.setAn(data[0]);
					acidProperties.setAv(data[1]);
					acidProperties.setProjectDetails(projectDetails);
					fluiddblist.add(acidProperties);
				}
			}
			
			if (line.startsWith("DIVERTER DATA")) {
				while ((line = br.readLine()) != null) {
					if (line.startsWith("/")) {
						break;
					}
					testDiverter = new TestDiverter();
					String[] data = line.split("=");
					testDiverter.setDname(data[0]);
					testDiverter.setDvalue(data[1]);
					testDiverter.setProjectDetails(projectDetails);
					dlist.add(testDiverter);
				}
			}
			
			acidPropertiesRepo.saveAll(fluiddblist);
			baseDiverterRepo.saveAll(bdlist);
			testDiverterRepo.saveAll(dlist);

		}

		System.out.println("File Found : " + br);
		br.close();
	}

}
