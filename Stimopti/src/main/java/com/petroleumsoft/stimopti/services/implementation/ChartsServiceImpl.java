package com.petroleumsoft.stimopti.services.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.Placement;
import com.petroleumsoft.stimopti.modal.Productivity;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.Skin;
import com.petroleumsoft.stimopti.repo.PenetrationRepo;
import com.petroleumsoft.stimopti.repo.PlacementRepo;
import com.petroleumsoft.stimopti.repo.ProductivityRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.SkinRepo;
import com.petroleumsoft.stimopti.services.ChartsService;

@Service("chartsService")
public class ChartsServiceImpl implements ChartsService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	PenetrationRepo penetrationRepo;
	@Autowired
	PlacementRepo placementRepo;
	@Autowired
	SkinRepo  skinRepo;
	@Autowired
	ProductivityRepo productivityRepo;

	@Override
	public List<Penetration> penetration(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Penetration> pentList = penetrationRepo.findByProjectDetails(details);
		return pentList;
	}

	@Override
	public List<Placement> placement(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Placement> placeList = placementRepo.findByProjectDetails(details);
		return placeList;
	}

	@Override
	public List<Skin> skin(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Skin> skinList = skinRepo.findByProjectDetails(details);
		return skinList;

	}

	@Override
	public List<Productivity> pi(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Productivity> prodList = productivityRepo.findByProjectDetails(details);
		return prodList;

	}

}
