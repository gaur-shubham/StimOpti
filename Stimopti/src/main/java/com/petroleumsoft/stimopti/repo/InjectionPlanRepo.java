package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
@Repository
public interface InjectionPlanRepo extends JpaRepository<InjectionPlan, Integer> {
	@Query("SELECT t FROM InjectionPlan t where t.projectDetails = :details order by t.id")
	public List<InjectionPlan> findByProjectDetails(ProjectDetails details);
	public void deleteByProjectDetailsAndId(ProjectDetails projectDetails,Integer id);
}
