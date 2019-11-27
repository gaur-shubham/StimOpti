package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.WellCompletion;

public interface WellCompletionService {
	public List<WellCompletion> changeComp(Integer pid,String cp);
	public String getCompletionType(Integer pid);

}
