package com.petroleumsoft.stimopti.controller;

import java.util.ArrayList;
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
import com.petroleumsoft.stimopti.repo.TestDiverterRepo;
import com.petroleumsoft.stimopti.services.DiverterService;

@Controller
@RequestMapping(value = "/diverters")
public class DivertersController {
	@Autowired
	private DiverterService diverterService;
	@Autowired
	private BaseDiverterRepo baseDiverterRepo;
	@Autowired
	private TestDiverterRepo testDiverterRepo;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;

	private static final String map = "diverters";

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@RequestParam("id") Integer pid, Model model, HttpSession session) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		if (baseDiverterRepo.findByProjectDetails(projectDetails).isEmpty()) {
			return map + "/list";
		}
		// adding BASE diverter to session and model
		session.setAttribute("bd", diverterService.getDiverterType(pid));
		model.addAttribute("bdlist", baseDiverterRepo.findByProjectDetails(projectDetails));
		// adding TEST diverter to session and model
		session.setAttribute("dtype", diverterService.getTestDirverterType(pid));
		model.addAttribute("viscolist", testDiverterRepo.findByProjectDetailsAndDtype(projectDetails, "VISCOSIFIED"));
		model.addAttribute("foamlist", testDiverterRepo.findByProjectDetailsAndDtype(projectDetails, "FOAMED"));
		model.addAttribute("partlist", testDiverterRepo.findByProjectDetailsAndDtype(projectDetails, "PARTICULATE"));
		model.addAttribute("veslist", testDiverterRepo.findByProjectDetailsAndDtype(projectDetails, "VES"));

		return map + "/showDiverters";
	}

	@RequestMapping(value = "/changebd", method = RequestMethod.POST)
	public String changebd(@RequestParam("pid") Integer pid, @RequestParam("bd") String bd,
			RedirectAttributes redirectAttributes) {
		diverterService.changebd(pid, bd);
		diverterService.removeTestDiverter(pid, bd.toUpperCase()); // Deleting TEST diverter (named as base diverter) if
																	// available.
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/diverters/list";
	}

	@PostMapping(value = "/bdedit")
	public String baseDiverterEdit(@RequestParam("pid") Integer pid, Model model) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("bdlist", baseDiverterRepo.findByProjectDetails(projectDetails));
		model.addAttribute("tdlist", new ArrayList());
		return map + "/edit";
	}

	@PostMapping(value = "/updatebd")
	public String updateBaseDiverter(@RequestParam("pid") Integer pid, @RequestParam("bdname") List<String> bdname,
			@RequestParam("bdvalue") List<String> bdvalue, RedirectAttributes redirectAttributes) {
		diverterService.updateDB(pid, bdname, bdvalue);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/diverters/list";
	}

	@PostMapping(value = "/addTestDiverter")
	public String addTestDiverter(@RequestParam("pid") Integer pid, @RequestParam("td") String td,
			RedirectAttributes redirectAttributes) {
		diverterService.saveTestDiverter(pid, td);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/diverters/list";
	}

	@PostMapping(value = "/delTestDiverter")
	public String delTestDiverter(@RequestParam("pid") Integer pid, @RequestParam("td") String td,
			RedirectAttributes redirectAttributes) {
		diverterService.removeTestDiverter(pid, td);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/diverters/list";
	}

	@PostMapping("/tdedit")
	public String testDiverterEdit(@RequestParam("pid") Integer pid, @RequestParam("td") String td, Model model) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("bdlist", new ArrayList());
		model.addAttribute("tdlist", testDiverterRepo.findByProjectDetailsAndDtype(projectDetails, td));
		model.addAttribute("td", td);
		return map + "/edit";
	}

	@PostMapping("/updatetd")
	public String updateTestDiverter(@RequestParam("pid") Integer pid, @RequestParam("tdname") List<String> tdname,
			@RequestParam("tdvalue") List<String> tdvalue, @RequestParam("td") String td,
			RedirectAttributes redirectAttributes) {
		diverterService.updateTD(pid, tdname, tdvalue, td);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/diverters/list";
	}
}
