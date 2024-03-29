package com.petroleumsoft.stimopti.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.PumpingEquipment;
@Repository
public interface PumpingEquipmentRepo extends JpaRepository<PumpingEquipment,Integer> {
	@Query("SELECT t FROM PumpingEquipment t where t.projectDetails = :projectDetails order by t.id")
	public List<PumpingEquipment> findByProjectDetails(ProjectDetails projectDetails);
	public void deleteByProjectDetails(ProjectDetails projectDetails);

}
