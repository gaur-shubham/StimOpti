/**
 * 
 */
package com.petroleumsoft.stimopti.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petroleumsoft.stimopti.modal.PotentialHazard;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.PotentialHazardRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.PotentialHazardService;

/**
 * @author ShubhamGaur
 *
 */
@Service
@Transactional
public class PotentialHazardServiceImp implements PotentialHazardService{

	@Autowired
	private PotentialHazardRepo hazardRepo;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	
	@Override
	public void saveHazard(Integer pid, List<String> phv) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<PotentialHazard> phlist=hazardRepo.findByProjectDetails(details);
		List<PotentialHazard> tempphlist=new ArrayList<>();
		if(!phlist.isEmpty()) {
			hazardRepo.deleteByProjectDetails(details);
		}
		
		for(int i=0;i<phv.size();i++) {
			PotentialHazard hazard=new PotentialHazard();
			if(!phv.get(i).replaceAll("[\"\\[\\]]", "").equalsIgnoreCase("")) {
				hazard.setHname(phv.get(i).replaceAll("[\"\\[\\]]", ""));
				hazard.setProjectDetails(details);
				tempphlist.add(hazard);
			}
			
		}
		if(tempphlist.size()>0) {
			hazardRepo.saveAll(tempphlist);
		}
		
	}


}
