package com.petroleumsoft.stimopti.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellCompletion;
import com.petroleumsoft.stimopti.modal.WellCompletionPerf;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellCompletionPerfRepo;
import com.petroleumsoft.stimopti.repo.WellCompletionRepo;
import com.petroleumsoft.stimopti.services.WellCompletionPerfService;
import com.petroleumsoft.stimopti.services.WellCompletionService;

@Controller
@RequestMapping(value = "/wellCompletion")
public class WellCompletionController {
	@Autowired
	private WellCompletionService WellCompletionService;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private WellCompletionRepo wellCompletionRepo;
	@Autowired
	private WellCompletionPerfService perfService;
	@Autowired
	private WellCompletionPerfRepo perfrepo;
	public static final String map = "wellCompletion";

	@RequestMapping("/list")
	public String list(@RequestParam("id") Integer pid, Model model, HttpSession session) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = wellCompletionRepo.findByProjectDetails(details);
		List<WellCompletionPerf> perfList = perfrepo.findByProjectDetails(details);
		if (!compList.isEmpty()) {
			model.addAttribute("compList", compList);
			model.addAttribute("perflist", perfList);
			session.setAttribute("cp", WellCompletionService.getCompletionType(pid));
			return map + "/showComp";
		}
		return map + "/list";
	}

	@RequestMapping(value = "/changeComp")
	public String changeComp(@RequestParam("pid") Integer pid, @RequestParam("wp") String cp, Model model,
			HttpSession session) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = WellCompletionService.changeComp(pid, cp);
		List<WellCompletionPerf> perfList = perfrepo.findByProjectDetails(details);
		model.addAttribute("compList", compList);
		model.addAttribute("perflist", perfList);
		session.setAttribute("cp", cp);
		return map + "/showComp";
	}

	@PostMapping(value = "/edit")
	public String edit(@RequestParam("pid") Integer pid, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = wellCompletionRepo.findByProjectDetails(details);
		List<WellCompletionPerf> perfList = perfrepo.findByProjectDetails(details);
		model.addAttribute("compList", compList);
		model.addAttribute("perflist", perfList);
		return map + "/edit";
	}

	//Updating Changes W Perf
	@PostMapping(value = "/saveupdate")
	public String saveUpdate(Model model, RedirectAttributes redirectAttributes, @RequestParam("pid") Integer pid,
			@RequestParam("cp") List<String> cp, @RequestParam("cv") List<String> cv,
			@RequestParam("startinput") List<String> startinput, @RequestParam("endinput") List<String> endinput) {
		WellCompletionService.saveUpdate(pid, cp, cv);
		perfService.saveUpdate(pid, startinput, endinput);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellCompletion/list";
	}

	//Updating Changes W/O Perf
	@PostMapping(value = "saveupdateWOperf")
	public String saveupdateWOperf(@RequestParam("pid") Integer pid, @RequestParam("cp") List<String> cp,
			@RequestParam("cv") List<String> cv, Model model, RedirectAttributes redirectAttributes) {
		WellCompletionService.saveUpdate(pid, cp, cv);
		perfService.saveUpdate(pid, new ArrayList<String>(), new ArrayList<String>());
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellCompletion/list";
	}
}
