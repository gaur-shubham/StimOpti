/**
 * 
 */
package com.petroleumsoft.stimopti.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.PotentialHazard;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.PotentialHazardRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.PotentialHazardService;

/**
 * @author ShubhamGaur
 *
 */
@Controller
@RequestMapping(value = "/potentialhazard")
public class PotentialHazardController {

	String map="potentialhazard";
	
	@Autowired
	private PotentialHazardRepo hazardRepo;
	@Autowired
	private PotentialHazardService hazardservice;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	
	@RequestMapping(value = "/list")
	public String list(@RequestParam("pid") Integer pid,Model model) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<PotentialHazard> tempphlist=hazardRepo.findByProjectDetails(details);
		List<String> phlist=new ArrayList<>();
		for(int i=0;i<tempphlist.size();i++) {
			phlist.add(tempphlist.get(i).getHname());
		}
		model.addAttribute("phlist", phlist);
		return map+"/list";
	}
	
	@PostMapping(value = "/updatehazard")
	public String updateHazard(@RequestParam("pid") Integer pid,@RequestParam("phv") List<String> phv,
			RedirectAttributes redirectAttributes) {
		hazardservice.saveHazard(pid, phv);
		redirectAttributes.addAttribute("pid", pid);
		return "redirect:/potentialhazard/list";
	}
}
