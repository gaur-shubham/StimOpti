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
	public void saveField(Integer pid, List<String> input,String type);
	public void deleteField(Integer pid,Integer id);
}
