package com.petroleumsoft.stimopti.controller;

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
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellCompletionRepo;
import com.petroleumsoft.stimopti.services.WellCompletionService;

@Controller
@RequestMapping(value = "/wellCompletion")
public class WellCompletionController {
	@Autowired
	WellCompletionService WellCompletionService;
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	WellCompletionRepo wellCompletionRepo;
	public static final String map = "wellCompletion";

	@RequestMapping("/list")
	public String list(@RequestParam("id") Integer pid, Model model,HttpSession session) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = wellCompletionRepo.findByProjectDetails(details);
		if (!compList.isEmpty()) {
			model.addAttribute("compList", compList);
			session.setAttribute("cp", WellCompletionService.getCompletionType(pid));
			return map + "/showComp";
		}
		return map + "/list";
	}

	@RequestMapping(value = "/changeComp")
	public String changeComp(@RequestParam("pid") Integer pid, @RequestParam("wp") String cp, Model model,
			HttpSession session) {
		List<WellCompletion> compList = WellCompletionService.changeComp(pid, cp);
		model.addAttribute("compList", compList);
		session.setAttribute("cp", cp);
		return map + "/showComp";
	}
	
	@PostMapping(value = "/edit")
	public String edit(@RequestParam("pid") Integer pid,Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<WellCompletion> compList = wellCompletionRepo.findByProjectDetails(details);
		model.addAttribute("compList", compList);
		return map + "/edit";
	}
	
	@PostMapping(value = "/saveupdate")
	public String saveUpdate(Model model,RedirectAttributes redirectAttributes,@RequestParam("pid") Integer pid) {
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/wellCompletion/list";
	}

}
