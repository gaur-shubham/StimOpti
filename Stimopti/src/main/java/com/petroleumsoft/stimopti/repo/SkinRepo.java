package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.Skin;

public interface SkinRepo extends JpaRepository<Skin, Integer> {
	@Query("SELECT t FROM Skin t where t.projectDetails = :projectDetails order by t.id")
	public List<Skin> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);


}
