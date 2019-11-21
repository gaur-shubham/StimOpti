package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.WellData;

public interface WellDataService {
	public List<WellData> saveWell(String wp,Integer pid);
	public void saveUpdate(Integer pid,List<String> wp,List<String> wv);

}
