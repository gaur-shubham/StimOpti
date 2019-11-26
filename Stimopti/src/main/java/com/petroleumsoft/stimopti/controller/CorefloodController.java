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

import com.petroleumsoft.stimopti.modal.Coreflood;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.CorefloodRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.CorefloodService;

@Controller
@RequestMapping(value = "coreflood")
public class CorefloodController {
	public static final String map = "coreflood";
	@Autowired
	CorefloodService corefloodService;
	@Autowired
	CorefloodRepo corefloodRepo;
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;

	@RequestMapping(value = "list",method = {RequestMethod.GET,RequestMethod.POST})
	public String list(@RequestParam("id") Integer pid, Model model,HttpSession session) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Coreflood> coreList = corefloodRepo.findByProjectDetails(details);
		if (!coreList.isEmpty()) {
			model.addAttribute("coreList", coreList);
			return map + "/cshow";
		} else {
			model.addAttribute("coreList", coreList);
			session.setAttribute("core", "Not Available");
			return map + "/cshow";
		}
	}

	@PostMapping(value = "changeCore")
	public String changeCore(@RequestParam("id") Integer pid, @RequestParam("cv") String cv, Model model,
			HttpSession session) {
		List<Coreflood> coreList = corefloodService.changeType(pid, cv);
		model.addAttribute("coreList", coreList);
		session.setAttribute("core", cv);
		return map + "/cshow";
	}
	
	@RequestMapping(value = "/edit",method = {RequestMethod.GET,RequestMethod.POST})
	public String edit(@RequestParam("pid") Integer pid,Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Coreflood> coreList = corefloodRepo.findByProjectDetails(details);
		model.addAttribute("coreList", coreList);
		return map + "/edit";
	}
	
	@PostMapping(value = "/saveupdate")
	public String saveupdate(@RequestParam("pid") Integer pid,@RequestParam("cn") List<String> cn
			,@RequestParam("cv") List<String> cv,RedirectAttributes redirectAttributes) {
		corefloodService.saveupdate(pid, cn, cv);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/coreflood/list";
	}

}
