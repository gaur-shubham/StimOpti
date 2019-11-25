/**
 * 
 */
package com.petroleumsoft.stimopti.services;

import java.util.List;

/**
 * @author ShubhamGaur
 *
 */
public interface InjectionPlanService {
	public void savefield(Integer pid, List<String> input);
	public void removeField(Integer pid,Integer id);
	public void saveUpdate(Integer pid,List<String> Oldinput, List<String> Newinput);
}
