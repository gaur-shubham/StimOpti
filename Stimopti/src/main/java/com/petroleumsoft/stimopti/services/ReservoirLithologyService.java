/**
 * 
 */
package com.petroleumsoft.stimopti.services;

import java.util.List;

/**
 * @author ShubhamGaur
 *
 */
public interface ReservoirLithologyService {
	public void saveField(Integer pid, List<String> input);
	public void deleteField(Integer pid,Integer id);
	public void saveupdate(Integer pid, List<String> Oldinput,List<String> Newinput);
	
}
