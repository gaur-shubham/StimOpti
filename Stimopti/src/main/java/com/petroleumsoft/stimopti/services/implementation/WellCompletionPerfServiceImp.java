/**
 * 
 */
package com.petroleumsoft.stimopti.services.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellCompletionPerf;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellCompletionPerfRepo;
import com.petroleumsoft.stimopti.services.WellCompletionPerfService;

/**
 * @author ShubhamGaur
 *
 */
@Service
@Transactional
public class WellCompletionPerfServiceImp implements WellCompletionPerfService {

	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private WellCompletionPerfRepo perfrepo;

	@Override
	public void saveUpdate(Integer pid, List<String> startinput, List<String> endinput) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletionPerf> compList = perfrepo.findByProjectDetails(projectDetails);
		List<WellCompletionPerf> tempcompList = new ArrayList<>();
		if (compList.size() > 0) {
			perfrepo.deleteByProjectDetails(projectDetails);
		}
		for (int i = 0; i < startinput.size(); i++) {
			WellCompletionPerf completionPerf = new WellCompletionPerf();
			completionPerf.setStart(startinput.get(i));
			completionPerf.setEnd(endinput.get(i));
			completionPerf.setProjectDetails(projectDetails);
			tempcompList.add(completionPerf);
		}
		perfrepo.saveAll(tempcompList);
	}

}
