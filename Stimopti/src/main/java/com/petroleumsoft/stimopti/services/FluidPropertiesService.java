package com.petroleumsoft.stimopti.services;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.petroleumsoft.stimopti.modal.FluidProperties;

public interface FluidPropertiesService {
	public String getFType(Integer pid);
	public List<FluidProperties> changeFluid(Integer pid,String wp);
	public void saveUpdate(Integer pid,List<String> fluidName,List<String> fluidValue);
}
