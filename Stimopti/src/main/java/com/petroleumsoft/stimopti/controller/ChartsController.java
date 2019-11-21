package com.petroleumsoft.stimopti.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.Placement;
import com.petroleumsoft.stimopti.modal.Productivity;
import com.petroleumsoft.stimopti.modal.Skin;
import com.petroleumsoft.stimopti.services.ChartsService;

@Controller
@RequestMapping("/charts")
public class ChartsController {
	@Autowired
	ChartsService chartsService;
	public static final String map = "charts";

	@RequestMapping("/show")
	public String show() {
		System.out.println("I am in charts show");
		return map + "/show";
	}

	@PostMapping("/penetration")
	public String penetration(@RequestParam("pid") Integer pid,Model model) {
		List<Penetration> penetList = chartsService.penetration(pid);
		model.addAttribute("penetList", penetList);
		return map+"/penetration";
	}
	@PostMapping("/placement")
	public String placement(@RequestParam("pid") Integer pid,Model model) {
		List<Placement> placeList = chartsService.placement(pid);
		model.addAttribute("placeList", placeList);
		return map+"/placement";
	}
	@PostMapping("/skin")
	public String skin(@RequestParam("pid") Integer pid,Model model) {
		List<Skin> skinList = chartsService.skin(pid);
		model.addAttribute("skinList", skinList);
		return map+"/skin";
	}
	@PostMapping("/productivity")
	public String productivity(@RequestParam("pid") Integer pid,Model model) {
		List<Productivity> prodList = chartsService.pi(pid);
		model.addAttribute("prodList", prodList);
		return map+"/productivity";
	}
}
