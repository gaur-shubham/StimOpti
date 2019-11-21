package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.ReservoirLithology;

@Repository
public interface ReservoirLithologyRepo extends JpaRepository<ReservoirLithology,Integer> {
	public List<ReservoirLithology> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetailsAndId(ProjectDetails projectDetails,Integer id);
}
