package com.petroleumsoft.stimopti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Stimopti {
	
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:/projectDetails/list";
	}
}
