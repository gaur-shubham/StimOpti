package com.petroleumsoft.stimopti.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.InjectionPlanRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.InjectionPlanService;
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
	@Autowired
	private InjectionPlanService injectionPlanService;
	private static final String map = "injectionplan";
	
	@RequestMapping(value="/list",method = {RequestMethod.GET,RequestMethod.POST})
	public String list(Model model,@ModelAttribute("injectionplan") InjectionPlan injectionplan,@RequestParam("id") Integer id) {
		System.out.println("i am in injection list  ");
		ProjectDetails projectDetails=projectDetailsRepository.findById(id).orElse(null);
		if(injectionPlanRepo.findByProjectDetails(projectDetails).isEmpty()) {
			model.addAttribute("injectionplan", new InjectionPlan());
			return  map + "/list";
		}
		model.addAttribute("injlist", injectionPlanRepo.findByProjectDetails(projectDetails));
		return map + "/show";
	}
	
	@PostMapping(value = "/importInjection")
	public String importInjection(Model model,@RequestParam("file") MultipartFile file,
			@RequestParam("id") Integer id,RedirectAttributes redirectAttributes) throws Exception {
		System.out.println("I am file : "+file+"  I am id : "+id);
		readExcelInjection.saveExcelinj(file, id);
		redirectAttributes.addAttribute("id", id);
		return "redirect:/injectionplan/list";
	}
	
	@PostMapping(value = "/savefield")
	public String saveField(Model model,@RequestParam("pid") Integer pid,@RequestParam("input") List<String> input) {
		injectionPlanService.savefield(pid, input);
		ProjectDetails projectDetails=projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("injlist", injectionPlanRepo.findByProjectDetails(projectDetails));
		return map + "/list";
	}
	
	@PostMapping("/removefield")
	public String removeField(Model model,@RequestParam("pid") Integer pid,@RequestParam("id") Integer id) {
		injectionPlanService.removeField(pid, id);
		ProjectDetails projectDetails=projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("injlist", injectionPlanRepo.findByProjectDetails(projectDetails));
		return map + "/list";
	}
	
	@RequestMapping("edit")
	public String edit(Model model,@RequestParam("pid") Integer pid) {
		ProjectDetails projectDetails=projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("injlist", injectionPlanRepo.findByProjectDetails(projectDetails));
		return map + "/edit";
	}
	
	@PostMapping(value = "/upsavefield")
	public String upSaveField(Model model,@RequestParam("pid") Integer pid,@RequestParam("Newinput") List<String> input,
			RedirectAttributes redirectAttributes) {
		injectionPlanService.savefield(pid, input);
		redirectAttributes.addAttribute("pid", pid);
		return "redirect:/injectionplan/edit";
	}
	
	@PostMapping("/upremovefield")
	public String upRemoveField(Model model,@RequestParam("pid") Integer pid,@RequestParam("id") Integer id,
			RedirectAttributes redirectAttributes) {
		injectionPlanService.removeField(pid, id);
		redirectAttributes.addAttribute("pid", pid);
		return "redirect:/injectionplan/edit";
	}
	
	@PostMapping("/upsaveall")
	public String upSaveAll(@RequestParam("pid") Integer pid,@RequestParam("Oldinput") List<String> Oldinput,
			@RequestParam("Newinput") List<String> Newinput,RedirectAttributes redirectAttributes) {
	injectionPlanService.saveUpdate(pid, Oldinput, Newinput);
	redirectAttributes.addAttribute("id", pid);
	return "redirect:/injectionplan/list";
	}
	
	@PostMapping("/upsavenew")
	public String upSaveNew(@RequestParam("pid") Integer pid,
			@RequestParam("Newinput") List<String> Newinput,RedirectAttributes redirectAttributes) {
		injectionPlanService.saveUpdate(pid, new ArrayList<String>(), Newinput);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/injectionplan/list";
	}
}
