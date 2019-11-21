package com.petroleumsoft.stimopti.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.ReservoirLithology;
import com.petroleumsoft.stimopti.repo.PenetrationRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.ReservoirLithologyRepo;
import com.petroleumsoft.stimopti.services.ReadExcel;

@Controller
@SessionAttributes(names = "details")
@RequestMapping(value = "/reservoirLithology")
public class ReservoirLithologyController {
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private ReservoirLithologyRepo reservoirLithologyRepo;
	@Autowired
	ReadExcel readExcel;
	@Autowired
	PenetrationRepo penetrationRepo;

	private static final String map = "reservoirLithology";

	@RequestMapping("/menu")
	public String menu(@ModelAttribute("details") ProjectDetails details, RedirectAttributes attributes,
			HttpSession httpSession, Model model) {
		httpSession.setAttribute("details", details);
		model.addAttribute("details", details);
		List<Penetration> penList = penetrationRepo.findByProjectDetails(details);
		if (penList.isEmpty()) {
			model.addAttribute("graph", false);
		} else {
			model.addAttribute("graph", true);
		}
		return map + "/menu";
	}

	@RequestMapping(value = "/redirectmenu")
	String redirectmenu(@ModelAttribute("project.id") Integer Id, RedirectAttributes attributes) {
		ProjectDetails details = projectDetailsRepository.findById(Id).orElse(null);
		attributes.addFlashAttribute("details", details);
		return "redirect:/reservoirLithology/menu";
	}

	@RequestMapping(value = "/list")
	public String list(@ModelAttribute("reservoirLithology") ReservoirLithology reservoirLithology,
			@RequestParam("id") Integer id, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		if (reservoirLithologyRepo.findByprojectDetails(details).isEmpty()) {
			model.addAttribute("reservoirLithology", new ReservoirLithology());
			return map + "/list";
		}
		model.addAttribute("rList", reservoirLithologyRepo.findByprojectDetails(details));
		return map + "/showlist";
	}

	@RequestMapping(value = "/showMenu/{Id}")
	public String showMenu(@PathVariable("Id") Integer id, RedirectAttributes attributes) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		attributes.addFlashAttribute("details", details);
		return "redirect:/reservoirLithology/menu";
	}

	@PostMapping("/importwelllog")
	public String importwelllog(Model model, @RequestParam("file") MultipartFile file, @RequestParam("id") Integer id,
			HttpSession session) throws Exception {
		System.out.println(" : Reading Lithology : ");
		List<ReservoirLithology> rList = readExcel.saveExcel(file, id);
		model.addAttribute("rList", rList);
		return map + "/showlist";
	}
}
