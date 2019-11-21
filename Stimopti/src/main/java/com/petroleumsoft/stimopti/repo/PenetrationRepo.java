package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface PenetrationRepo extends JpaRepository<Penetration, Integer>{
	public List<Penetration> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
	

}
