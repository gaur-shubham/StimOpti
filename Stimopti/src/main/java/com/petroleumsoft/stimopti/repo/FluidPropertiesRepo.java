package com.petroleumsoft.stimopti.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.petroleumsoft.stimopti.modal.FluidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface FluidPropertiesRepo extends JpaRepository<FluidProperties, Integer> {
	public List<FluidProperties> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);
}
