/**
 * 
 */
package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.PotentialHazard;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

/**
 * @author ShubhamGaur
 *
 */
@Repository
public interface PotentialHazardRepo extends JpaRepository<PotentialHazard, Integer>{
	@Query("SELECT t FROM PotentialHazard t where t.projectDetails = :projectDetails order by t.id")
	public List<PotentialHazard> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
}
