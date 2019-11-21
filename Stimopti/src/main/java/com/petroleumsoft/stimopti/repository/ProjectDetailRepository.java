package com.petroleumsoft.stimopti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.ProjectDetails;

@Repository
public interface ProjectDetailRepository extends JpaRepository<ProjectDetails, Integer>{

}
