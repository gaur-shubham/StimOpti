package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.FormationDamage;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface FormationDamageRepo extends JpaRepository<FormationDamage, Integer> {
	@Query("SELECT t FROM FormationDamage t where t.projectDetails = :details order by t.id")
	public List<FormationDamage> findByprojectDetails(ProjectDetails details);
}
