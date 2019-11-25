package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.Placement;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface PlacementRepo extends JpaRepository<Placement, Integer>{
	@Query("SELECT t FROM Placement t where t.projectDetails = :projectDetails order by t.id")
	public List<Placement> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);

}
