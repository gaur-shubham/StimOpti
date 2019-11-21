package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petroleumsoft.stimopti.modal.Coreflood;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface CorefloodRepo extends JpaRepository<Coreflood, Integer> {
	public List<Coreflood> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);

}
