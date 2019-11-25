/**
 * 
 */
package com.petroleumsoft.stimopti.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.InjectionPlanRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.InjectionPlanService;

/**
 * @author ShubhamGaur
 *
 */
@Service
@Transactional
public class InjectionPlanServiceImp implements InjectionPlanService {
	
	@Autowired
	ProjectDetailsRepository projectDetailsRepo;
	@Autowired
	InjectionPlanRepo injectionRepo;
	
	@Override
	public void savefield(Integer pid, List<String> input) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		List<InjectionPlan> Datalist = injectionRepo.findByProjectDetails(projectDetails);
		InjectionPlan injectionPlan=new InjectionPlan();
		injectionPlan.setDuration(input.get(0));
		injectionPlan.setStage(input.get(1));
		injectionPlan.setStavageVolBbls(input.get(2));
		injectionPlan.setPumpRateBMP(input.get(3));
		injectionPlan.setInjPressPSI(input.get(4));
		injectionPlan.setBhppsi(input.get(5));
		injectionPlan.setDirection(input.get(6));
		injectionPlan.setInjDefthFT(input.get(7));
		injectionPlan.setProjectDetails(projectDetails);
		injectionRepo.save(injectionPlan);
	}

	@Override
	public void removeField(Integer pid, Integer id) {
			ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
			injectionRepo.deleteByProjectDetailsAndId(projectDetails, id);
	}

	@Override
	public void saveUpdate(Integer pid, List<String> oldInput, List<String> newInput) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		List<InjectionPlan> Datalist = injectionRepo.findByProjectDetails(projectDetails);
		List<InjectionPlan> tempDatalist = new ArrayList<InjectionPlan>();
		List<String> tempInput = new ArrayList<>();
		if(!(oldInput.isEmpty()||newInput.contains(""))) {
			tempInput.addAll(oldInput);
			tempInput.addAll(newInput);
		}
		else if (newInput.contains("")&&!oldInput.isEmpty()) {
			tempInput.addAll(oldInput);
		} else if(!newInput.contains("")&&oldInput.isEmpty()){
			tempInput.addAll(newInput);
		}
		int j = 0;
		InjectionPlan injectionPlan=null;
		for (int i = 0; i < tempInput.size(); i++) {
			if (j < Datalist.size()) {
				injectionPlan = Datalist.get(j);
				j++;
			} else {
				injectionPlan = new InjectionPlan();
			}
			injectionPlan.setDuration(tempInput.get(i));
			injectionPlan.setStage(tempInput.get(++i));
			injectionPlan.setStavageVolBbls(tempInput.get(++i));
			injectionPlan.setPumpRateBMP(tempInput.get(++i));
			injectionPlan.setInjPressPSI(tempInput.get(++i));
			injectionPlan.setBhppsi(tempInput.get(++i));
			injectionPlan.setDirection(tempInput.get(++i));
			injectionPlan.setInjDefthFT(tempInput.get(++i));
			injectionPlan.setProjectDetails(projectDetails);
			tempDatalist.add(injectionPlan);
		}
		injectionRepo.saveAll(tempDatalist);
	}
	
	

}
