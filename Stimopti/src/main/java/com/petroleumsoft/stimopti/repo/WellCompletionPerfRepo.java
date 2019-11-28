/**
 * 
 */
package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellCompletionPerf;

/**
 * @author ShubhamGaur
 *
 */

@Repository
public interface WellCompletionPerfRepo extends JpaRepository<WellCompletionPerf, Integer>{
	
	@Query("SELECT t FROM WellCompletionPerf t where t.projectDetails = :projectDetails order by t.id")
	public List<WellCompletionPerf> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
}
