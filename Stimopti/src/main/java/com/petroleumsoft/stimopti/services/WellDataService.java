package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.WellData;

public interface WellDataService {
	public String getWellType(Integer pid);
	public List<WellData> saveWellProfile(String wp,Integer pid);
	public void saveUpdate(Integer pid,List<String> wp,List<String> wv);
	public void saveCompletionType(String ct,Integer pid);
	public String getWellCompletionType(Integer pid);
	public void updateCompletionType(Integer pid,List<String> cn,List<String> cv);
}
