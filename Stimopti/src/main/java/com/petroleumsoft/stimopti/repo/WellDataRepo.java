package com.petroleumsoft.stimopti.repo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellData;

public interface WellDataRepo extends JpaRepository<WellData, Integer> {
	@Query("SELECT t FROM WellData t where t.projectDetails = :details order by t.id")
	public List<WellData> findByProjectDetails(ProjectDetails details);
	public void deleteByProjectDetails(ProjectDetails details); 
	
	

}
