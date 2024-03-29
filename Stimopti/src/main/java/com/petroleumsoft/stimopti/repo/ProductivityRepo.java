package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.Productivity;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface ProductivityRepo extends JpaRepository<Productivity, Integer> {
	@Query("SELECT t FROM Productivity t where t.projectDetails = :projectDetails order by t.id")
	public List<Productivity> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);


}
