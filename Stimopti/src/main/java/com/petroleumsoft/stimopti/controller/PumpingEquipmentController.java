package com.petroleumsoft.stimopti.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.PumpingEquipment;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.PumpingEquipmentRepo;
import com.petroleumsoft.stimopti.services.PumpingEquipmentService;

@Controller
@RequestMapping(value = "/pumpingEquipment")
public class PumpingEquipmentController {
	private static final String map = "pumpingEquipment";
	@Autowired
	ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	PumpingEquipmentRepo pumpingEquipmentRepo;
	@Autowired
	PumpingEquipmentService pumpingEquipmentService;

	@RequestMapping(value = "/list")
	public String list(Model model,@RequestParam("id") Integer pid) {
		ProjectDetails projectDetails=projectDetailsRepository.findById(pid).orElse(null);
		if(pumpingEquipmentRepo.findByProjectDetails(projectDetails).isEmpty()) {
			model.addAttribute("pumpingEquipment", new PumpingEquipment());
			return map+"/list";
		}
		System.out.println("I am here");
		model.addAttribute("pelist", pumpingEquipmentRepo.findByProjectDetails(projectDetails));
		return map + "/showPump";
	}

	@RequestMapping(value = "/savePump", method = RequestMethod.POST)
	public String savePump(Model model,@RequestParam("pv") String pv,
			@RequestParam("pid") Integer pid,HttpSession session) {
		List<PumpingEquipment> pelist = pumpingEquipmentService.savePumping(pv,pid);
		System.out.println("Pe :   "+pelist);
		model.addAttribute("pelist", pelist);
		session.setAttribute("pv",pv);
		return map + "/showPump";
	}

}
