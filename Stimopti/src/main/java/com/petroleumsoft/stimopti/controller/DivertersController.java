package com.petroleumsoft.stimopti.controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.BaseDiverter;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.BaseDiverterRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.DiverterService;

@Controller
@RequestMapping(value = "/diverters")
public class DivertersController {
	@Autowired
	DiverterService diverterService;
	@Autowired
	BaseDiverterRepo baseDiverterRepo;
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	private static final String map = "diverters";

	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String list(@RequestParam("id") Integer pid, Model model,HttpSession session) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		if (baseDiverterRepo.findByProjectDetails(projectDetails).isEmpty()) {
			return map + "/list";
		}
		session.setAttribute("bd", diverterService.getDiverterType(pid));
		model.addAttribute("bdlist",baseDiverterRepo.findByProjectDetails(projectDetails));
		return map + "/showDiverters";
	}

	@RequestMapping(value = "/changebd", method = RequestMethod.POST)
	public String changebd(@RequestParam("pid") Integer pid, @RequestParam("bd") String bd, Model model,HttpSession session) {
		List<BaseDiverter> bdlist = diverterService.changebd(pid, bd);
		model.addAttribute("bdlist", bdlist);
		session.setAttribute("bd", bd);
		System.out.println("Base Diverter : " + bdlist);
		return map + "/showDiverters";
	}
	
	@RequestMapping(value = "/edit", method = {RequestMethod.GET,RequestMethod.POST})
	public String edit(@RequestParam("pid") Integer pid,Model model) {
		System.out.println("pid>>"+pid);
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		List<BaseDiverter> bdlist =baseDiverterRepo.findByProjectDetails(projectDetails);
		model.addAttribute("bdlist", bdlist);
		return map + "/edit";
	}
	
	@PostMapping(value = "/saveupdate")
	public String saveUpdate(@RequestParam("pid") Integer pid,@RequestParam("bdname") List<String> bdname,
			@RequestParam("bdvalue") List<String> bdvalue,RedirectAttributes redirectAttributes) {
		diverterService.saveUpdate(pid, bdname, bdvalue);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/diverters/list";
	}
}
