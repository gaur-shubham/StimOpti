package com.petroleumsoft.stimopti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.FormationDamage;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.FormationDamageRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.FormationDamageService;

@Controller
@RequestMapping(value="/formationDamage")
public class FormationDamageController {
	private static final String map="formationDamage";
	@Autowired
	private ProjectDetailsRepository projectDetailsRepo;
	@Autowired
	private FormationDamageService formationDamageService;
	@Autowired
	private FormationDamageRepo damagerepo;
	@RequestMapping(value="/list",method = {RequestMethod.GET,RequestMethod.POST})
	public String list(@RequestParam("id") Integer pid,Model model) {
		ProjectDetails details = projectDetailsRepo.findById(pid).orElse(null);
		List<FormationDamage> fdlist=damagerepo.findByprojectDetails(details);
		model.addAttribute("fdlist",fdlist);
		return map+"/list";
	}
	
	@RequestMapping(value = "/edit")
	public String edit(@RequestParam("pid") Integer pid,Model model) {
		ProjectDetails details = projectDetailsRepo.findById(pid).orElse(null);
		model.addAttribute("fdlist",damagerepo.findByprojectDetails(details)); 
		return map+"/edit";
	}
	
	@PostMapping(value = "/saveupdate")
	public String saveUpdate(@RequestParam("pid") Integer pid,@RequestParam("fdname") List<String> fdname,
			@RequestParam("fdvalue") List<String> fdvalue,RedirectAttributes redirectAttributes) {
		formationDamageService.saveUpdate(pid, fdname, fdvalue);
		redirectAttributes.addAttribute("id",pid);
		return "redirect:/formationDamage/list";
	}
}
