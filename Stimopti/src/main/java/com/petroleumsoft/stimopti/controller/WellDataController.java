package com.petroleumsoft.stimopti.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellData;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.WellDataRepo;
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

	private static final String map = "wellData";

	@RequestMapping(value = "/list")
	public String list(Model model, @RequestParam("id") Integer pid) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		if (wellDataRepo.findByProjectDetails(projectDetails).isEmpty()) {
			model.addAttribute("wellData", new WellData());
			return map + "/list";
		}
		model.addAttribute("wellDatalist", wellDataRepo.findByProjectDetails(projectDetails));
		return map + "/showWell";
	}

	@PostMapping(value = "/changeWell")
	public String changeWell(Model model, @RequestParam("wp") String wp, @RequestParam("pid") Integer pid,
			HttpSession session) {
		List<WellData> wellDatalist = wellDataService.saveWell(wp, pid);
		model.addAttribute("wellDatalist", wellDatalist);
		session.setAttribute("wp", wp);
		return map + "/showWell";
	}

	@PostMapping("/edit")
	public String edit(@RequestParam("pid") Integer pid, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("wellDatalist", wellDataRepo.findByProjectDetails(details));
		return map + "/edit";
	}
	
	@PostMapping("/updateWell")
	public String updateWell(Model model,@RequestParam("pid") Integer pid,@RequestParam("wv") List<String> wv,@RequestParam("wp") List<String> wp) {
		wellDataService.saveUpdate(pid, wp, wv);
		ProjectDetails projectDetails = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("wellDatalist", wellDataRepo.findByProjectDetails(projectDetails));
		return map + "/showWell";
	}
}
