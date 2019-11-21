package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
@Repository
public interface InjectionPlanRepo extends JpaRepository<InjectionPlan, Integer> {
	public List<InjectionPlan> findByprojectDetails(ProjectDetails details);
}
