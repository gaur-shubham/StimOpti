package com.petroleumsoft.stimopti.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.FluidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface FluidPropertiesRepo extends JpaRepository<FluidProperties, Integer> {
	@Query("SELECT t FROM FluidProperties t where t.projectDetails = :projectDetails order by t.id")
	public List<FluidProperties> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
}
