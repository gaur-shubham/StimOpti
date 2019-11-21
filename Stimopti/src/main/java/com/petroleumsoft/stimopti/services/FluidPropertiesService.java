package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.FluidProperties;

public interface FluidPropertiesService {
	
	public List<FluidProperties> changeFluid(Integer pid,String wp);

}
