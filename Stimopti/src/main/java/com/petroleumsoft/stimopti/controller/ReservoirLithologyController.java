package com.petroleumsoft.stimopti.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.petroleumsoft.stimopti.services.ReservoirLithologyService;

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
	@Autowired
	ReservoirLithologyService lithologyService;

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

	@RequestMapping(value = "/list" , method = { RequestMethod.POST, RequestMethod.GET })
	public String list(@ModelAttribute("reservoirLithology") ReservoirLithology reservoirLithology,
			@RequestParam("id") Integer id, Model model) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		if (reservoirLithologyRepo.findByProjectDetails(details).isEmpty()) {
			model.addAttribute("reservoirLithology", new ReservoirLithology());
			model.addAttribute("details", details);
			return map + "/list";
		}
		model.addAttribute("rList", reservoirLithologyRepo.findByProjectDetails(details));
		model.addAttribute("details", details);
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
			HttpSession session,RedirectAttributes redirectAttributes) throws Exception {
		System.out.println(" : Reading Lithology : ");
		readExcel.saveExcel(file, id);
		redirectAttributes.addAttribute("id", id);
		return "redirect:/reservoirLithology/list";
	}
	
	@PostMapping("/savefield")
	public String savefield(Model model,@RequestParam("pid") Integer pid,@RequestParam("input") List<String> input,
			RedirectAttributes redirectAttributes) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		lithologyService.saveField(pid, input);
		model.addAttribute("rList", reservoirLithologyRepo.findByProjectDetails(details));
		return  map + "/list";
	}
	
	@PostMapping("/removefield")
	public String removefield(Model model,@RequestParam("pid") Integer pid,@RequestParam("id") Integer id,
			RedirectAttributes redirectAttributes) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		lithologyService.deleteField(pid, id);
		model.addAttribute("rList", reservoirLithologyRepo.findByProjectDetails(details));
		return  map + "/list";
	}
	
	@RequestMapping(value="/edit" , method = { RequestMethod.POST, RequestMethod.GET })
	public String edit(Model model,@RequestParam("pid") Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		model.addAttribute("details", details);
		model.addAttribute("rList", reservoirLithologyRepo.findByProjectDetails(details));
		return map + "/edit";
	}
	
	@PostMapping("/upsavefield")
	public String upSavefield(Model model,@RequestParam("pid") Integer pid,@RequestParam("Newinput") List<String> input,
			RedirectAttributes redirectAttributes) {
		lithologyService.saveField(pid, input);
		redirectAttributes.addAttribute("pid", pid);
		return "redirect:/reservoirLithology/edit";
	}
	
	@PostMapping("/upremovefield")
	public String upDemovefield(Model model,@RequestParam("pid") Integer pid,@RequestParam("id") Integer id,
			RedirectAttributes redirectAttributes) {
		lithologyService.deleteField(pid, id);
		redirectAttributes.addAttribute("pid", pid);
		return "redirect:/reservoirLithology/edit";
	}
	
	@PostMapping("/upsaveall")
	public String upsaveall(Model model, @RequestParam("pid") Integer pid, @RequestParam("Oldinput") List<String> Oldinput,
			@RequestParam("Newinput") List<String> Newinput,RedirectAttributes redirectAttributes) {
		lithologyService.saveupdate(pid, Oldinput, Newinput);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/reservoirLithology/list";
	}
	
	@PostMapping("/upsavenew")
	public String upsavenew(Model model, @RequestParam("pid") Integer pid,
			@RequestParam("Newinput") List<String> Newinput,RedirectAttributes redirectAttributes) {
		lithologyService.saveupdate(pid, new ArrayList<String>(), Newinput);
		redirectAttributes.addAttribute("id", pid);
		return "redirect:/reservoirLithology/list";
	}
}
