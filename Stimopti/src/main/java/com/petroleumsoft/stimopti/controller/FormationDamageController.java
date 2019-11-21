package com.petroleumsoft.stimopti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.petroleumsoft.stimopti.modal.FormationDamage;
import com.petroleumsoft.stimopti.services.FormationDamageService;

@Controller
@RequestMapping(value="/formationDamage")
public class FormationDamageController {
	private static final String map="formationDamage";
	@Autowired
	private FormationDamageService formationDamageService;
	@RequestMapping(value="/list")
	public String list(@RequestParam("id") Integer pid,Model model) {
		List<FormationDamage> fdlist=formationDamageService.saveFormation(pid);
		model.addAttribute("fdlist",fdlist);
		return map+"/list";
	}

}
