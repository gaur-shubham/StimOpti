package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.BaseDiverter;

public interface DiverterService {
	public String getDiverterType(Integer pid);

	public int bId(Integer id);

	public int dId(Integer id);

	public List<String> showbd(Integer pid);

	public List<String> showtd(Integer pid);

	public List<BaseDiverter> changebd(Integer pid, String name);

	public List<String> changetd(Integer pid, Integer bid, String name);

	public void updateDB(Integer pid, List<String> bdname, List<String> bdvalue);

	public void saveTestDiverter(Integer pid, String td);

	public void removeTestDiverter(Integer pid, String td);

	public List<String> getTestDirverterType(Integer pid);

	public void updateTD(Integer pid, List<String> tdname, List<String> tdvalue, String td);
}
