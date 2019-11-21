package com.petroleumsoft.stimopti.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String list(@RequestParam("id") Integer id, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		List<WellCompletion> compList = wellCompletionRepo.findByProjectDetails(details);
		if (!compList.isEmpty()) {
			model.addAttribute("compList", compList);
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

}
