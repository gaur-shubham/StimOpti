package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.AcidProperties;

public interface AcidPropertiesService {
	public List<AcidProperties> acidchangelist(Integer id,String acidname);
	public void saveupdate(Integer pid,List<String> an,List<String> av);
	

}
