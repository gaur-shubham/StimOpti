package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellCompletion;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellCompletionRepo;
import com.petroleumsoft.stimopti.services.WellCompletionService;

@Service("wellCompletionService")
@Transactional
public class WellCompletionServiceImplementation implements WellCompletionService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	WellCompletionRepo wellCompletionRepo;

	@Override
	public List<WellCompletion> changeComp(Integer pid, String cp) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = new ArrayList<WellCompletion>();
		if (!wellCompletionRepo.findByProjectDetails(projectDetails).isEmpty()) {
			wellCompletionRepo.deleteByProjectDetails(projectDetails);
		}
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(cp)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						WellCompletion wc = new WellCompletion();
						String[] data = line.split("=");
						wc.setCp(data[0]);
						wc.setCv(data[1]);
						wc.setProjectDetails(projectDetails);
						compList.add(wc);
					}
					wellCompletionRepo.saveAll(compList);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}

		return compList;
	}

	@Override
	public String getCompletionType(Integer pid) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = wellCompletionRepo.findByProjectDetails(projectDetails);
		if(compList.get(0).getCv().equalsIgnoreCase("Open Hole")) {
			return "Open Hole";
		}else if(compList.get(0).getCv().equalsIgnoreCase("Cased Hole")) {
			return "Cased Hole";
		}else if(compList.get(0).getCv().equalsIgnoreCase("Slotted Liner")) {
			return "Slotted Liner";
		}
		return null;
	}

}
