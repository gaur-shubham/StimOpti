package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.petroleumsoft.stimopti.modal.FormationDamage;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.FormationDamageRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.FormationDamageService;

@Service("formationDamageService")
public class FormationDamageServiceImplementation implements FormationDamageService {
@Autowired
ProjectDetailsRepository projectDetailsRepository;
@Autowired
FormationDamageRepo formationDamageRepo;
	@Override
	public List<FormationDamage> saveFormation(Integer pid) {
		ProjectDetails details=projectDetailsRepository.findById(pid).orElse(null);
		List<FormationDamage> fdlist=new ArrayList<FormationDamage>();
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith("Formation Damage and Mineralogy")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						FormationDamage fd = new FormationDamage();
						String[] data = line.split("=");
						fd.setFdname(data[0]);
						fd.setFdvalue(data[1]);
						fd.setProjectDetails(details);
						fdlist.add(fd);
					}
					formationDamageRepo.saveAll(fdlist);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}
		 
		return fdlist;
	}

}
