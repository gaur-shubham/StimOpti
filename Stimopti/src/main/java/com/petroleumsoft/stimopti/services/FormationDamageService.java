package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.FormationDamage;

public interface FormationDamageService {
	public List<FormationDamage> saveFormation(Integer pid);
	public void saveUpdate(Integer pid, List<String> fdname,List<String> fdvalue);
}
