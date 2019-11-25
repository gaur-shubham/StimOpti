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

import com.petroleumsoft.stimopti.modal.BaseDiverter;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.BaseDiverterRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.DiverterService;

@Service("diverterService")
@Transactional
public class DiverterServiceImplementation implements DiverterService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	BaseDiverterRepo baseDiverterRepo;

	@Override
	public int bId(Integer id) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		List<BaseDiverter> baseDiverter = baseDiverterRepo.findByProjectDetails(details);
		for (BaseDiverter bs : baseDiverter) {
			if (bs.getBdname().equals("DIVERTER TYPE")) {
				id = bs.getId();
			}
		}
		return id;
	}

	@Override
	public int dId(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> showbd(Integer pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> showtd(Integer pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseDiverter> changebd(Integer pid, String name) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<BaseDiverter> bdlist = new ArrayList<BaseDiverter>();
		if (baseDiverterRepo.findByProjectDetails(details) != null) {
			
			baseDiverterRepo.deleteByProjectDetails(details);
		}

		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(name)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						BaseDiverter bd = new BaseDiverter();
						String[] data = line.split("=");
						bd.setBdname(data[0]);
						bd.setBdvalue(data[1]);
						bd.setProjectDetails(details);
						bdlist.add(bd);
					}
					baseDiverterRepo.saveAll(bdlist);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}

		return bdlist;
	}

	@Override
	public List<String> changetd(Integer pid, Integer bid, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUpdate(Integer pid, List<String> bdname, List<String> bdvalue) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<BaseDiverter> bdlist = baseDiverterRepo.findByProjectDetails(details);
		List<BaseDiverter> tempbdlist = new ArrayList<BaseDiverter>();
		
		for(int i=0;i<bdlist.size();i++) {
			BaseDiverter baseDiverter=bdlist.get(i);
			baseDiverter.setBdvalue(bdvalue.get(bdname.indexOf(bdlist.get(i).getBdname())));
			baseDiverter.setProjectDetails(details);
			tempbdlist.add(baseDiverter);
		}
		baseDiverterRepo.saveAll(tempbdlist);
	}

}
