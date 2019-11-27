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
	public String list(@RequestParam("id") Integer pid, Model model,HttpSession session) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<FluidProperties> fList = fluidPropertiesRepo.findByProjectDetails(details);
		if (fList.isEmpty()) {
			return map + "/list";
		}
		session.setAttribute("ftype", fluidPropertiesService.getFType(pid));
		model.addAttribute("fList", fList);
		return map + "/showFluid";
		
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
	
	@PostMapping(value = "/edit")
	public String edit(@RequestParam("pid") Integer pid, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<FluidProperties> fList = fluidPropertiesRepo.findByProjectDetails(details);
		model.addAttribute("fList", fList);
		return map + "/edit";
	}
	
	@PostMapping(value = "/update")
	public String update(@RequestParam("pid") Integer pid,@RequestParam("fluidName") List<String> fluidName
			,@RequestParam("fluidValue") List<String> fluidValue,RedirectAttributes redirectAttributes) {
		fluidPropertiesService.saveUpdate(pid, fluidName, fluidValue);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/fluidProperties/list";
	}
}
