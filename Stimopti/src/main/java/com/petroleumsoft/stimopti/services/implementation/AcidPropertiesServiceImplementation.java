package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import com.petroleumsoft.stimopti.modal.AcidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.AcidPropertiesRepo;
import com.petroleumsoft.stimopti.repository.ProjectDetailRepository;
import com.petroleumsoft.stimopti.services.AcidPropertiesService;

/* @Transactional must needed for customized delete in SpringBoot JPA .*/

@Service("acidPropertiesService")
@Transactional
public class AcidPropertiesServiceImplementation implements AcidPropertiesService {
	@Autowired
	private ProjectDetailRepository projectDetailsRepository;
	@Autowired
	private AcidPropertiesRepo acidPropertiesRepo;

	@Override
	public List<AcidProperties> acidchangelist(Integer id, String acidname) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(id).orElse(null);
		List<AcidProperties> acidlist = new ArrayList<AcidProperties>();
		if (acidPropertiesRepo.findByProjectDetails(projectDetails) != null) {
			acidPropertiesRepo.deleteByProjectDetails(projectDetails);
		}
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(acidname)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						AcidProperties ap = new AcidProperties();
						String[] data = line.split("=");
						ap.setAn(data[0]);
						ap.setAv(data[1]);
						ap.setProjectDetails(projectDetails);
						acidlist.add(ap);
					}
					acidPropertiesRepo.saveAll(acidlist);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}

		return acidlist;
	}

	@Override
	public void saveupdate(Integer pid, List<String> an, List<String> av) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		List<AcidProperties> acidlist = acidPropertiesRepo.findByProjectDetails(projectDetails);
		List<AcidProperties> tempacidlist = new ArrayList<AcidProperties>();
		
		for(int i=0;i<acidlist.size();i++) {
			AcidProperties acidProperties=acidlist.get(i);
			acidProperties.setAv(av.get(an.indexOf(acidlist.get(i).getAn())));
			tempacidlist.add(acidProperties);
		}
		acidPropertiesRepo.saveAll(tempacidlist);
	}

	

}
