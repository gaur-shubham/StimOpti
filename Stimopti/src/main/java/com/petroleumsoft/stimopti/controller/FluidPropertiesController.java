package com.petroleumsoft.stimopti.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.petroleumsoft.stimopti.modal.FluidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.FluidPropertiesRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.FluidPropertiesService;

@Controller
@RequestMapping(value = "fluidProperties")
public class FluidPropertiesController {
	@Autowired
	FluidPropertiesService fluidPropertiesService;
	@Autowired
	FluidPropertiesRepo fluidPropertiesRepo;
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	private static final String map = "fluidProperties";

	@RequestMapping(value = "/list")
	public String list(@RequestParam("id") Integer id, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		List<FluidProperties> fList = fluidPropertiesRepo.findByProjectDetails(details);
		if (fList != null) {
			model.addAttribute("fList", fList);
			return map + "/showFluid";

		}

		return map + "/list";
	}

	@RequestMapping(value = "/changefluid")
	public String changefluid(Model model, @RequestParam("pid") Integer pid, @RequestParam("wp") String wp,
			HttpSession session) {
		System.out.println("Rendering from Change Fluid :" + pid + " = " + wp);
		List<FluidProperties> fList = fluidPropertiesService.changeFluid(pid, wp);
		model.addAttribute("fList", fList);
		session.setAttribute("ftype", wp);
		return map + "/showFluid";
	}
}
