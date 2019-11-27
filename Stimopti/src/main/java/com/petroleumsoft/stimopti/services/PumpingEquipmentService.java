package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.PumpingEquipment;

public interface PumpingEquipmentService {
	public String getInjectionType(Integer pid);
	public List<PumpingEquipment> savePumping(String pv,Integer id);
	public void update(Integer pid,List<String> pp,List<String> pv);

}
