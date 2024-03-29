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
import com.petroleumsoft.stimopti.modal.FluidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.FluidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.FluidPropertiesService;

@Service("fluidPropertiesService")
@Transactional
public class FluidPropertiesServiceImplementation implements FluidPropertiesService {
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	FluidPropertiesRepo fluidPropertiesRepo;

	@Override
	public List<FluidProperties> changeFluid(Integer pid, String wp) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		if(fluidPropertiesRepo.findByProjectDetails(details) !=null) {
			fluidPropertiesRepo.deleteByProjectDetails(details);	
		}
		List<FluidProperties> fList= new ArrayList<FluidProperties>();
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
						FluidProperties fp = new FluidProperties();
						String[] data = line.split("=");
						fp.setFluidName(data[0]);
						fp.setFluidValue(data[1]);
						fp.setProjectDetails(details);
						fp.setFluidType(wp);
						fList.add(fp);
					}
					fluidPropertiesRepo.saveAll(fList);
					break;
				}
			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}
		return fList;
	}

	@Override
	public void saveUpdate(Integer pid, List<String> fluidName, List<String> fluidValue) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<FluidProperties> fList=fluidPropertiesRepo.findByProjectDetails(details);
		List<FluidProperties> tempfList=new ArrayList<>();
		for(int i=0;i<fList.size();i++) {
			FluidProperties fluidProperties=fList.get(i);
			fluidProperties.setFluidValue(fluidValue.get(fluidName.indexOf(fList.get(i).getFluidName())));
			fluidProperties.setProjectDetails(details);
			tempfList.add(fluidProperties);
		}
		fluidPropertiesRepo.saveAll(tempfList);
	}

	@Override
	public String getFType(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<FluidProperties> fList=fluidPropertiesRepo.findByProjectDetails(details);
		if(fList.get(0).getFluidValue().equalsIgnoreCase("gas")) {
			return "Gas";
		}else if(fList.get(0).getFluidValue().equalsIgnoreCase("oil")) {
			return "Oil";
		}
		return null;
	}

	@Override
	public List<String> getFluidParam(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<FluidProperties> fList=fluidPropertiesRepo.findByProjectDetails(details);
		List<String> temp=new ArrayList<String>();
		for(int i=0;i<fList.size();i++) {
			if(!temp.contains(fList.get(i).getFluidName())) {
				temp.add(fList.get(i).getFluidName());
			}
		}
		return temp;
	}
	

}
