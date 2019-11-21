package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petroleumsoft.stimopti.modal.FormationDamage;
import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface FormationDamageRepo extends JpaRepository<FormationDamage, Integer> {
public List<FormationDamage> findByprojectDetails(ProjectDetails details);
}
