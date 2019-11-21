package com.petroleumsoft.stimopti.controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

	@RequestMapping(value = "/list")
	public String list(@RequestParam("id") Integer pid, Model model) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		if (baseDiverterRepo.findByProjectDetails(projectDetails).isEmpty()) {
			return map + "/list";
		}
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
}
