package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface PenetrationRepo extends JpaRepository<Penetration, Integer>{
	@Query("SELECT t FROM Penetration t where t.projectDetails = :projectDetails order by t.id")
	public List<Penetration> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
	

}
