package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.petroleumsoft.stimopti.modal.AcidProperties;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
@Repository
public interface AcidPropertiesRepo extends JpaRepository<AcidProperties, Integer> {
	public List<AcidProperties> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);

}
