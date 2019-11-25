package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.BaseDiverter;

public interface DiverterService {
	public int bId(Integer id);
	public int dId(Integer id);
	public List<String> showbd(Integer pid);
	public List<String> showtd(Integer pid);
	public List<BaseDiverter> changebd(Integer pid,String name);
	public List<String> changetd(Integer pid,Integer bid,String name);
	public void saveUpdate(Integer pid,List<String> bdname,List<String> bdvalue);
}
