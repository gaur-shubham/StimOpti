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
import com.petroleumsoft.stimopti.modal.Coreflood;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.CorefloodRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.CorefloodService;

@Service("corefloodService")
@Transactional
public class CorefloodServiceImpl implements CorefloodService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	CorefloodRepo corefloodRepo;

	@Override
	public List<Coreflood> changeType(Integer pid, String cv) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Coreflood> coreList = new ArrayList<Coreflood>();
		if (corefloodRepo.findByProjectDetails(details) != null) {
			corefloodRepo.deleteByProjectDetails(details);
			System.out.println("Deleted Core");
		}
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(cv)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						Coreflood cf = new Coreflood();
						String[] data = line.split("=");
						cf.setCn(data[0]);
						cf.setCv(data[1]);
						cf.setProjectDetails(details);
						coreList.add(cf);
					}
					corefloodRepo.saveAll(coreList);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}
		return coreList;
	}

	@Override
	public List<Coreflood> setNotAvailable(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Coreflood> coreList = new ArrayList<Coreflood>();
		String cv="Not Available";
		if (corefloodRepo.findByProjectDetails(details) != null) {
			corefloodRepo.deleteByProjectDetails(details);
		}
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(cv)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						Coreflood cf = new Coreflood();
						String[] data = line.split("=");
						cf.setCn(data[0]);
						cf.setCv(data[1]);
						cf.setProjectDetails(details);
						coreList.add(cf);
					}
					corefloodRepo.saveAll(coreList);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}
		return coreList;
	}

}
