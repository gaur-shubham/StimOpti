package com.petroleumsoft.stimopti.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.DefaultDataService;
import com.petroleumsoft.stimopti.services.FormationDamageService;
import com.petroleumsoft.stimopti.services.GenerateInputService;

@Controller
@RequestMapping(value = "/projectDetails")
public class ProjectDetailsController {
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	DefaultDataService defaultDataService;
	@Autowired
	GenerateInputService generateInputService;
	@Autowired
	private FormationDamageService formationDamageService;
	
	private final String mapping = "/projectDetails";

	@RequestMapping("/list")
	public String list(Model model) {
		System.out.println("i am in list ");
		Direction direction = Direction.DESC;
		List<ProjectDetails> projectList = projectDetailsRepository.findAll(Sort.by(direction, "DateCreated"));
		model.addAttribute("projectListInstance", projectList);
		return mapping + "/list";
	}

	@RequestMapping("/create")
	public String create(@ModelAttribute("projectDetails") ProjectDetails projectDetails, Model model) {
		model.addAttribute("projectDetails", new ProjectDetails());
		return mapping + "/create";
	}

	@RequestMapping(value = "/save")
	public String save(@ModelAttribute("projectDetails") ProjectDetails projectDetails, RedirectAttributes attributes) {
		Date d = new Date();
		Timestamp ts = new Timestamp(d.getTime());
		projectDetails.setDateCreated(ts);
		projectDetails.setModule("CARBONATE ACIDIZING");
		projectDetailsRepository.save(projectDetails);
		int pid = projectDetails.getId();
		formationDamageService.saveFormation(pid);
		try {
			defaultDataService.setDefault(pid);
		} catch (Exception e) {
			System.out.println(e);
		}
		attributes.addFlashAttribute("project.id", projectDetails.getId());
		return "redirect:/reservoirLithology/redirectmenu";
	}

	@RequestMapping(value = "/generateInput")
	public String  generateInput(@RequestParam("id") Integer id,RedirectAttributes attributes) {
		System.out.println("I am in generate input file Method");
		generateInputService.generateInput(id);
		attributes.addFlashAttribute("project.id",id);
		return "redirect:/reservoirLithology/redirectmenu";
	}
	
	@RequestMapping(value = "/delete/{pid}")
	public String deleteProject(@PathVariable("pid") Integer pid, Model model) {
		projectDetailsRepository.deleteById(pid);
		Direction direction = Direction.DESC;
		List<ProjectDetails> projectList = projectDetailsRepository.findAll(Sort.by(direction, "DateCreated"));
		model.addAttribute("projectListInstance", projectList);
		return mapping + "/list";
	}

}
