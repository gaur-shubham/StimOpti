/**
 * 
 */
package com.petroleumsoft.stimopti.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.ReservoirLithology;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.ReservoirLithologyRepo;
import com.petroleumsoft.stimopti.services.ReservoirLithologyService;

/**
 * @author ShubhamGaur
 *
 */
@Service
@Transactional
public class ReservoirLithologyServImp implements ReservoirLithologyService{

	@Autowired
	ProjectDetailsRepository projectDetailsRepo;
	@Autowired
	ReservoirLithologyRepo repo;
	@Override
	public void saveField(Integer pid, List<String> input, String type) {
			ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
			List<ReservoirLithology> reservoirdata=new ArrayList<>();
			ReservoirLithology lithology=new ReservoirLithology();
			List<String> tempInput=new ArrayList<>();
			if(type.equalsIgnoreCase("import")) {
				tempInput=new ArrayList<String>(input);
			}else if(type.equalsIgnoreCase("update")) {
				int n=input.size()-8;
				for(int i=n;i<input.size();i++) {
					tempInput.add(input.get(i));
				}
			}
				lithology.setFromDefthFT(tempInput.get(0));
				lithology.setToDefthFT(tempInput.get(1));
				lithology.settVDFT(tempInput.get(2));
				lithology.setPermMD(tempInput.get(3));
				lithology.setPoro(tempInput.get(4));
				lithology.setZonePressPSI(tempInput.get(5));
				lithology.setPrestimskin(tempInput.get(6));
				lithology.setPrestimpi(tempInput.get(7));
				lithology.setProjectDetails(projectDetails);
			repo.save(lithology);
	}
	@Override
	public void deleteField(Integer pid, Integer id) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		repo.deleteByProjectDetailsAndId(projectDetails, id);
	}
	
	
}
