package com.petroleumsoft.stimopti.repo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.WellData;

public interface WellDataRepo extends JpaRepository<WellData, Integer> {
	public List<WellData> findByProjectDetails(ProjectDetails details);
	public void deleteByProjectDetails(ProjectDetails details); 
	
	

}
