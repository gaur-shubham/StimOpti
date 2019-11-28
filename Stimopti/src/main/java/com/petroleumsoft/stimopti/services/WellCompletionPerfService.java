/**
 * 
 */
package com.petroleumsoft.stimopti.services;

import java.util.List;

/**
 * @author ShubhamGaur
 *
 */
public interface WellCompletionPerfService {
	public void saveUpdate(Integer pid,List<String> startinput,List<String> endinput);
}
