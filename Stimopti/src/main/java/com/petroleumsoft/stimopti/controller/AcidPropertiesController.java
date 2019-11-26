package com.petroleumsoft.stimopti.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.petroleumsoft.stimopti.modal.AcidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.AcidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.AcidPropertiesService;

@Controller
@RequestMapping(value = "/acidProperties")
public class AcidPropertiesController {
	private static final String map = "acidProperties";
	@Autowired
	AcidPropertiesService acidPropertiesService;
	@Autowired
	AcidPropertiesRepo acidPropertiesRepo;
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;

	@RequestMapping(value = "/list")
	public String list(@RequestParam("id") Integer id, Model model) {
		ProjectDetails details=projectDetailsRepository.findById(id).orElse(null);
		List<AcidProperties> aclist =acidPropertiesRepo.findByProjectDetails(details); 
		String[] acarr= {"Hcl 5","Emulsified Acid","Hcl 5","Hcl 7.5","Hcl 10", "Hcl 15","Hcl 28","Alcoholic Acid 10","Alcoholic Acid 15","Alcoholic Acid 28","Alcoholic Acid 5","Alcoholic Acid 7.5","Hcl-Acetic Acid 10/10","Hcl-Acetic Acid 10/5","Hcl-Acetic Acid 15/","Hcl-Acetic Acid 5/5","Hcl-Acetic Acid 7.5/5","Mud Acid 4/0.5","Mud Acid 4.5/0.5","Mud Acid 10/1","Mud Acid 10/1.5","Mud Acid 10/2","Mud Acid 12/1.5","Mud Acid 12/2","Mud Acid 12/3","Mud Acid 12/6","Mud Acid 6/0.5","Mud Acid 6/1","Mud Acid 6/1.5","Mud Acid 7.5/1.5","Mud Acid 8/0.5","Mud Acid 8/1","Mud Acid 8/2","Mud Acid 9/1","Mud Acid 9/1.5"};
		List<String> newacarr=new ArrayList<String>();
		for (String element : acarr) { 
            if (!newacarr.contains(element)) { 
            	newacarr.add(element); 
            } 
        } 
		model.addAttribute("acidp", aclist);
		model.addAttribute("anamelist",newacarr);
		return map + "/list";
	}
	@PostMapping("/changelistAcid")
	public String changelistAcid(@RequestParam("id")Integer id,@RequestParam("aval") String acidname,Model model) {
		List<AcidProperties> aclist = acidPropertiesService.acidchangelist(id, acidname);
		String[] acarr= {acidname,"Emulsified Acid","Hcl 5","Hcl 7.5","Hcl 10", "Hcl 15","Hcl 28","Alcoholic Acid 10","Alcoholic Acid 15","Alcoholic Acid 28","Alcoholic Acid 5","Alcoholic Acid 7.5","Hcl-Acetic Acid 10/10","Hcl-Acetic Acid 10/5","Hcl-Acetic Acid 15/","Hcl-Acetic Acid 5/5","Hcl-Acetic Acid 7.5/5","Mud Acid 4/0.5","Mud Acid 4.5/0.5","Mud Acid 10/1","Mud Acid 10/1.5","Mud Acid 10/2","Mud Acid 12/1.5","Mud Acid 12/2","Mud Acid 12/3","Mud Acid 12/6","Mud Acid 6/0.5","Mud Acid 6/1","Mud Acid 6/1.5","Mud Acid 7.5/1.5","Mud Acid 8/0.5","Mud Acid 8/1","Mud Acid 8/2","Mud Acid 9/1","Mud Acid 9/1.5"};
		List<String> newacarr=new ArrayList<String>();
		for (String element : acarr) { 
            if (!newacarr.contains(element)) { 
            	newacarr.add(element); 
            } 
        } 
		model.addAttribute("acidp", aclist);
		model.addAttribute("anamelist",newacarr);
		return map+"/list";
	}
	
	@PostMapping("/edit")
	public String edit(@RequestParam("id")Integer id,@RequestParam("aval") String acidname,Model model) {
		ProjectDetails details=projectDetailsRepository.findById(id).orElse(null);
		List<AcidProperties> aclist =acidPropertiesRepo.findByProjectDetails(details); 
		String[] acarr= {acidname,"Emulsified Acid","Hcl 5","Hcl 7.5","Hcl 10", "Hcl 15","Hcl 28","Alcoholic Acid 10","Alcoholic Acid 15","Alcoholic Acid 28","Alcoholic Acid 5","Alcoholic Acid 7.5","Hcl-Acetic Acid 10/10","Hcl-Acetic Acid 10/5","Hcl-Acetic Acid 15/","Hcl-Acetic Acid 5/5","Hcl-Acetic Acid 7.5/5","Mud Acid 4/0.5","Mud Acid 4.5/0.5","Mud Acid 10/1","Mud Acid 10/1.5","Mud Acid 10/2","Mud Acid 12/1.5","Mud Acid 12/2","Mud Acid 12/3","Mud Acid 12/6","Mud Acid 6/0.5","Mud Acid 6/1","Mud Acid 6/1.5","Mud Acid 7.5/1.5","Mud Acid 8/0.5","Mud Acid 8/1","Mud Acid 8/2","Mud Acid 9/1","Mud Acid 9/1.5"};
		List<String> newacarr=new ArrayList<String>();
		for (String element : acarr) { 
            if (!newacarr.contains(element)) { 
            	newacarr.add(element); 
            } 
        } 
		model.addAttribute("acidp", aclist);
		model.addAttribute("anamelist",newacarr);
		return map + "/edit";
	}
	
	@PostMapping("/changeEditAcid")
	public String changeEditAcid(@RequestParam("id")Integer id,@RequestParam("aval") String acidname,Model model) {
		List<AcidProperties> aclist = acidPropertiesService.acidchangelist(id, acidname);
		String[] acarr= {acidname,"Emulsified Acid","Hcl 5","Hcl 7.5","Hcl 10", "Hcl 15","Hcl 28","Alcoholic Acid 10","Alcoholic Acid 15","Alcoholic Acid 28","Alcoholic Acid 5","Alcoholic Acid 7.5","Hcl-Acetic Acid 10/10","Hcl-Acetic Acid 10/5","Hcl-Acetic Acid 15/","Hcl-Acetic Acid 5/5","Hcl-Acetic Acid 7.5/5","Mud Acid 4/0.5","Mud Acid 4.5/0.5","Mud Acid 10/1","Mud Acid 10/1.5","Mud Acid 10/2","Mud Acid 12/1.5","Mud Acid 12/2","Mud Acid 12/3","Mud Acid 12/6","Mud Acid 6/0.5","Mud Acid 6/1","Mud Acid 6/1.5","Mud Acid 7.5/1.5","Mud Acid 8/0.5","Mud Acid 8/1","Mud Acid 8/2","Mud Acid 9/1","Mud Acid 9/1.5"};
		List<String> newacarr=new ArrayList<String>();
		for (String element : acarr) { 
			if (!newacarr.contains(element)) { 
				newacarr.add(element); 
			} 
		} 
		model.addAttribute("acidp", aclist);
		model.addAttribute("anamelist",newacarr);
		return map + "/edit";
	}

	@PostMapping("/updateAcid")
	public String updateAcid(@RequestParam("pid") Integer pid, @RequestParam("an") List<String> an,
			@RequestParam("av") List<String> av,Model model) {
	acidPropertiesService.saveupdate(pid, an, av);
	ProjectDetails details=projectDetailsRepository.findById(pid).orElse(null);
	List<AcidProperties> aclist =acidPropertiesRepo.findByProjectDetails(details); 
	String[] acarr= {"Hcl 5","Emulsified Acid","Hcl 5","Hcl 7.5","Hcl 10", "Hcl 15","Hcl 28","Alcoholic Acid 10","Alcoholic Acid 15","Alcoholic Acid 28","Alcoholic Acid 5","Alcoholic Acid 7.5","Hcl-Acetic Acid 10/10","Hcl-Acetic Acid 10/5","Hcl-Acetic Acid 15/","Hcl-Acetic Acid 5/5","Hcl-Acetic Acid 7.5/5","Mud Acid 4/0.5","Mud Acid 4.5/0.5","Mud Acid 10/1","Mud Acid 10/1.5","Mud Acid 10/2","Mud Acid 12/1.5","Mud Acid 12/2","Mud Acid 12/3","Mud Acid 12/6","Mud Acid 6/0.5","Mud Acid 6/1","Mud Acid 6/1.5","Mud Acid 7.5/1.5","Mud Acid 8/0.5","Mud Acid 8/1","Mud Acid 8/2","Mud Acid 9/1","Mud Acid 9/1.5"};
	List<String> newacarr=new ArrayList<String>();
	for (String element : acarr) { 
        if (!newacarr.contains(element)) { 
        	newacarr.add(element); 
        } 
    } 
	model.addAttribute("acidp", aclist);
	model.addAttribute("anamelist",newacarr);
	return map+"/list";
	}
}
