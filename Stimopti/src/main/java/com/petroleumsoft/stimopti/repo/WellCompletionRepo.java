package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellCompletion;

public interface WellCompletionRepo extends JpaRepository<WellCompletion, Integer>{
	public List<WellCompletion> findByProjectDetails(ProjectDetails details);
	public void deleteByProjectDetails(ProjectDetails projectDetails); 
	
}
