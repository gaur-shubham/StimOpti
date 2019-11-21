package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.petroleumsoft.stimopti.modal.FluidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.FluidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.FluidPropertiesService;

@Service("fluidPropertiesService")
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

}
