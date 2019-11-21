package com.petroleumsoft.stimopti.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.TestDiverter;

@Repository
public interface TestDiverterRepo extends JpaRepository<TestDiverter, Integer> {
	public List<TestDiverter> findByProjectDetails(ProjectDetails projectDetails);
}
