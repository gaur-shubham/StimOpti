package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.Coreflood;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface CorefloodRepo extends JpaRepository<Coreflood, Integer> {
	@Query("SELECT t FROM Coreflood t where t.projectDetails = :projectDetails order by t.id")
	public List<Coreflood> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);

}
