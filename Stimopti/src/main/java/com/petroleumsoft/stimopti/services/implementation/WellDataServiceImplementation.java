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
import com.petroleumsoft.stimopti.modal.WellData;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellDataRepo;
import com.petroleumsoft.stimopti.services.WellDataService;

@Service("wellDataService")
@Transactional
public class WellDataServiceImplementation implements WellDataService {
	@Autowired
	WellDataRepo wellDataRepo;
	@Autowired
	ProjectDetailsRepository projectDetailsRepo;

	@Override
	public List<WellData> saveWell(String wp, Integer pid) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		List<WellData> wellDatalist = new ArrayList<WellData>();
		if (wellDataRepo.findByProjectDetails(projectDetails) != null) {
			wellDataRepo.deleteByProjectDetails(projectDetails);
			wellDataRepo.flush();
			System.out.println("Deleted Well Data");
		}
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(wp)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						WellData wd = new WellData();
						String[] data = line.split("=");
						wd.setWp(data[0]);
						wd.setWv(data[1]);
						wd.setProjectDetails(projectDetails);
						wellDatalist.add(wd);
					}
					wellDataRepo.saveAll(wellDatalist);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}

		return wellDatalist;
	}

	@Override
	public void saveUpdate(Integer pid, List<String> wp, List<String> wv) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		List<WellData> wellDatalist = wellDataRepo.findByProjectDetails(projectDetails);
		List<WellData> tempwellDatalist = new ArrayList<WellData>();
		for(int i=0;i<wellDatalist.size();i++) {
			WellData data=wellDatalist.get(i);
			data.setWv(wv.get(wp.indexOf(wellDatalist.get(i).getWp())));
			tempwellDatalist.add(data);
		}
		wellDataRepo.saveAll(tempwellDatalist);
	}

	@Override
	public String getWellType(Integer pid) {
		ProjectDetails projectDetails = projectDetailsRepo.findById(pid).orElse(null);
		List<WellData> wellDatalist = wellDataRepo.findByProjectDetails(projectDetails);
		if(wellDatalist.get(0).getWv().equalsIgnoreCase("Vertical")) {
			return "Vertical";
		}else if(wellDatalist.get(0).getWv().equalsIgnoreCase("Horizontal")) {
			return "Horizontal";
		}else if(wellDatalist.get(0).getWv().equalsIgnoreCase("Slanted")) {
			return "Slanted";
		}
		return null;
	}

}
