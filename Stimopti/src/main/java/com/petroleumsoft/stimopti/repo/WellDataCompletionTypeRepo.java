/**
 * 
 */
package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellDataCompletionType;

/**
 * @author ShubhamGaur
 *
 */
@Repository
public interface WellDataCompletionTypeRepo extends JpaRepository<WellDataCompletionType, Integer>{
	@Query("SELECT t FROM WellDataCompletionType t where t.projectDetails = :projectDetails order by t.id")
	public List<WellDataCompletionType> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
}
