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
public class ReservoirLithologyServImp implements ReservoirLithologyService {

	@Autowired
	ProjectDetailsRepository projectDetailsRepo;
	@Autowired
	ReservoirLithologyRepo repo;

	@Override
	public void saveField(Integer pid, List<String> input) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		ReservoirLithology lithology = new ReservoirLithology();
		lithology.setFromDefthFT(input.get(0));
		lithology.setToDefthFT(input.get(1));
		lithology.settVDFT(input.get(2));
		lithology.setPermMD(input.get(3));
		lithology.setPoro(input.get(4));
		lithology.setZonePressPSI(input.get(5));
		lithology.setPrestimskin(input.get(6));
		lithology.setPrestimpi(input.get(7));
		lithology.setProjectDetails(projectDetails);
		repo.save(lithology);
	}

	@Override
	public void deleteField(Integer pid, Integer id) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		repo.deleteByProjectDetailsAndId(projectDetails, id);
	}

	@Override
	public void saveupdate(Integer pid, List<String> Oldinput, List<String> Newinput) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		List<ReservoirLithology> list = repo.findByProjectDetails(projectDetails);
		List<ReservoirLithology> templist = new ArrayList<>();
		List<String> tempInput = new ArrayList<>();
		if(!(Oldinput.isEmpty()||Newinput.contains(""))) {
			tempInput.addAll(Oldinput);
			tempInput.addAll(Newinput);
		}
		else if (Newinput.contains("")&&!Oldinput.isEmpty()) {
			tempInput.addAll(Oldinput);
		} else if(!Newinput.contains("")&&Oldinput.isEmpty()){
			tempInput.addAll(Newinput);
		}
		int j = 0;
		ReservoirLithology lithology = null;
		for (int i = 0; i < tempInput.size(); i++) {
			if (j < list.size()) {
				lithology = list.get(j);
				j++;
			} else {
				lithology = new ReservoirLithology();
			}
			lithology.setFromDefthFT(tempInput.get(i));
			lithology.setToDefthFT(tempInput.get(++i));
			lithology.settVDFT(tempInput.get(++i));
			lithology.setPermMD(tempInput.get(++i));
			lithology.setPoro(tempInput.get(++i));
			lithology.setZonePressPSI(tempInput.get(++i));
			lithology.setPrestimskin(tempInput.get(++i));
			lithology.setPrestimpi(tempInput.get(++i));
			lithology.setProjectDetails(projectDetails);
			templist.add(lithology);
		}
		repo.saveAll(templist);
	}

}
