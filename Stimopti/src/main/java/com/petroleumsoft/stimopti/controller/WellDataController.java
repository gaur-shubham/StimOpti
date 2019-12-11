package com.petroleumsoft.stimopti.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellData;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellCompletionPerfRepo;
import com.petroleumsoft.stimopti.repo.WellDataCompletionTypeRepo;
import com.petroleumsoft.stimopti.repo.WellDataRepo;
import com.petroleumsoft.stimopti.services.WellCompletionPerfService;
import com.petroleumsoft.stimopti.services.WellCompletionService;
import com.petroleumsoft.stimopti.services.WellDataService;

@Controller
@RequestMapping("/wellData")
public class WellDataController {
	@Autowired
	WellDataService wellDataService;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private WellDataRepo wellDataRepo;
	@Autowired
	private WellDataCompletionTypeRepo completionTypeRepo;
	@Autowired
	private WellCompletionPerfRepo perfrepo;
	@Autowired
	private WellCompletionPerfService perfService;
	@Autowired
	private WellCompletionService WellCompletionService;

	private static final String map = "wellData";

	@RequestMapping(value = "/list")
	public String list(Model model, @RequestParam("id") Integer pid,HttpSession session) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		if (wellDataRepo.findByProjectDetails(projectDetails).isEmpty()) {
			model.addAttribute("wellData", new WellData());
			return map + "/list";
		}
		session.setAttribute("wp", wellDataService.getWellType(pid));
		session.setAttribute("ct", wellDataService.getWellCompletionType(pid));
		model.addAttribute("wellDatalist", wellDataRepo.findByProjectDetails(projectDetails));
		model.addAttribute("compType", completionTypeRepo.findByProjectDetails(projectDetails));
		model.addAttribute("perflist", perfrepo.findByProjectDetails(projectDetails));
		return map + "/showWell";
	}

	@PostMapping(value = "/changewellprofile")
	public String changeWellProfile(Model model, @RequestParam("wp") String wp, @RequestParam("pid") Integer pid,
			RedirectAttributes redirectAttributes) {
		wellDataService.saveWellProfile(wp, pid);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellData/list";
	}

	@PostMapping("/editwp")
	public String editWellProfile(@RequestParam("pid") Integer pid, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("wellDatalist", wellDataRepo.findByProjectDetails(details));
		model.addAttribute("compType", new ArrayList<>());
		model.addAttribute("perflist", perfrepo.findByProjectDetails(details));
		return map + "/edit";
	}
	@PostMapping("/editct")
	public String editCompType(@RequestParam("pid") Integer pid, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("wellDatalist", new ArrayList<>());
		model.addAttribute("compType", completionTypeRepo.findByProjectDetails(details));
		model.addAttribute("perflist", perfrepo.findByProjectDetails(details));
		return map + "/edit";
	}
	
	@PostMapping("/updatewellprofile")
	public String updateWellProfile(Model model,@RequestParam("pid") Integer pid,
			@RequestParam("wv") List<String> wv,@RequestParam("wp") List<String> wp,
			RedirectAttributes redirectAttributes) {
		wellDataService.saveUpdate(pid, wp, wv);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellData/list";
	}
	
	@PostMapping("/updatecomptype")
	public String updateCompletionType(Model model,@RequestParam("pid") Integer pid,
			@RequestParam("cv") List<String> cv,@RequestParam("cn") List<String> cn,
			RedirectAttributes redirectAttributes,@RequestParam("startinput") List<String> startinput,
			@RequestParam("endinput") List<String> endinput) {
		wellDataService.updateCompletionType(pid, cn, cv);
		perfService.saveUpdate(pid, startinput, endinput);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellData/list";
	}
	
	@PostMapping(value = "/savecomptypeWOperf")
	public String savecomptypeWOperf(@RequestParam("pid") Integer pid, @RequestParam("cn") List<String> cp,
			@RequestParam("cv") List<String> cv, Model model, RedirectAttributes redirectAttributes) {
		wellDataService.updateCompletionType(pid, cp, cv);
		perfService.saveUpdate(pid, new ArrayList<String>(), new ArrayList<String>());
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellData/list";
	}
	
	
	@PostMapping("/changecomptype")
	public String changeCompletionType(@RequestParam("ct") String ct, @RequestParam("pid") Integer pid,
			RedirectAttributes redirectAttributes) {
		wellDataService.saveCompletionType(ct, pid);
		WellCompletionService.changeComp(pid, ct);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellData/list";
	}
}
