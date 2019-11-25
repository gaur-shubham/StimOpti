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

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.PumpingEquipment;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.PumpingEquipmentRepo;
import com.petroleumsoft.stimopti.services.PumpingEquipmentService;
@Service("pumpingEquipmentService")
@Transactional
public class PumpingEquipmentServiceImplementation implements PumpingEquipmentService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepo;
	@Autowired
	PumpingEquipmentRepo pumpingEquipmentRepo;

	@Override
	public List<PumpingEquipment> savePumping(String pv, Integer id) {
		System.out.println("I am in pe service implementation ");
		ProjectDetails projectDetails=projectDetailsRepo.findById(id).orElse(null);
		List<PumpingEquipment> peList = new ArrayList<PumpingEquipment>();
		if(pumpingEquipmentRepo.findByProjectDetails(projectDetails) !=null) {
			pumpingEquipmentRepo.deleteByProjectDetails(projectDetails);
		}
		System.out.println("Pv :    "+pv);
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(pv)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						PumpingEquipment pe = new PumpingEquipment();
						String[] data = line.split("=");
						pe.setPp(data[0]);
						pe.setPv(data[1]);
						pe.setProjectDetails(projectDetails);
						peList.add(pe);
					}
					pumpingEquipmentRepo.saveAll(peList);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}
		return peList;
	}

	@Override
	public void update(Integer pid, List<String> pp, List<String> pv) {
			ProjectDetails projectDetails=projectDetailsRepo.findById(pid).orElse(null);
			List<PumpingEquipment> peList =pumpingEquipmentRepo.findByProjectDetails(projectDetails);
			List<PumpingEquipment> temppeList =new ArrayList<PumpingEquipment>();
			for(int i=0;i<peList.size();i++) {
				PumpingEquipment equipment=peList.get(i);
				equipment.setPv(pv.get(pp.indexOf(peList.get(i).getPp())));
				temppeList.add(equipment);
			}
			pumpingEquipmentRepo.saveAll(temppeList);
	}

	
}
