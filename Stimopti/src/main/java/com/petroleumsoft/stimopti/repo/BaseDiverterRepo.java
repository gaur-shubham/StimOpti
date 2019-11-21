package com.petroleumsoft.stimopti.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.petroleumsoft.stimopti.modal.BaseDiverter;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

@Repository
public interface BaseDiverterRepo extends JpaRepository<BaseDiverter, Integer> {
	public List<BaseDiverter> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);

}
