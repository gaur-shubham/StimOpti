package com.petroleumsoft.stimopti.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.InjectionPlanRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.ReadExcelInjection;

@Controller
@RequestMapping(value = "/injectionplan")
public class InjectionPlanController {
	@Autowired
	private ReadExcelInjection readExcelInjection;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private InjectionPlanRepo injectionPlanRepo;
	private static final String map = "injectionplan";
	@RequestMapping(value="/list")
	public String list(Model model,@ModelAttribute("injectionplan") InjectionPlan injectionplan,@RequestParam("id") Integer id) {
		System.out.println("i am in injection list  ");
		ProjectDetails projectDetails=projectDetailsRepository.findById(id).orElse(null);
		if(injectionPlanRepo.findByprojectDetails(projectDetails).isEmpty()) {
			model.addAttribute("injectionplan", new InjectionPlan());
			return  map + "/list";
		}
		model.addAttribute("injlist", injectionPlanRepo.findByprojectDetails(projectDetails));
		return map + "/show";
		
	}
	@RequestMapping(value = "/importInjection",method =RequestMethod.POST)
	public String importInjection(Model model,@RequestParam("file") MultipartFile file,@RequestParam("id") Integer id) throws Exception {
		System.out.println("I am file : "+file+"  I am id : "+id);
		
		List<InjectionPlan> injlist=readExcelInjection.saveExcelinj(file, id);
		model.addAttribute("injlist", injlist);
		return map+"/show";
	}

}
