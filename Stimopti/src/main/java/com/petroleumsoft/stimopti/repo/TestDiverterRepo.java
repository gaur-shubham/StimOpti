package com.petroleumsoft.stimopti.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.TestDiverter;

@Repository
public interface TestDiverterRepo extends JpaRepository<TestDiverter, Integer> {
	@Query("SELECT t FROM TestDiverter t where t.projectDetails = :projectDetails order by t.id")
	public List<TestDiverter> findByProjectDetails(ProjectDetails projectDetails);
}
