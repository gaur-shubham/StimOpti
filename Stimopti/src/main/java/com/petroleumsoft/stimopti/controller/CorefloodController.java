package com.petroleumsoft.stimopti.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping("list")
	public String list(@RequestParam("id") Integer pid, Model model,HttpSession session) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<Coreflood> coreList = corefloodRepo.findByProjectDetails(details);
		if (!coreList.isEmpty()) {
			model.addAttribute("coreList", coreList);
			return map + "/cshow";
		} else {
			coreList = corefloodService.setNotAvailable(pid);
			model.addAttribute("coreList", coreList);
			session.setAttribute("core", "Not Available");
			return map + "/cshow";
		}
	}

	@RequestMapping(value = "changeCore")
	public String changeCore(@RequestParam("id") Integer pid, @RequestParam("cv") String cv, Model model,
			HttpSession session) {
		List<Coreflood> coreList = corefloodService.changeType(pid, cv);
		model.addAttribute("coreList", coreList);
		session.setAttribute("core", cv);
		return map + "/cshow";
	}

}
