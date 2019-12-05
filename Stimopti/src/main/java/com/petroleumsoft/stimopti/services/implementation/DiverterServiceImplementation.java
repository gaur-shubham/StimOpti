package com.petroleumsoft.stimopti.services.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.petroleumsoft.stimopti.modal.BaseDiverter;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.TestDiverter;
import com.petroleumsoft.stimopti.repo.BaseDiverterRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.TestDiverterRepo;
import com.petroleumsoft.stimopti.services.DiverterService;

@Service("diverterService")
@Transactional
public class DiverterServiceImplementation implements DiverterService {
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private BaseDiverterRepo baseDiverterRepo;
	@Autowired
	private TestDiverterRepo testDiverterRepo;

	@Override
	public int bId(Integer id) {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		List<BaseDiverter> baseDiverter = baseDiverterRepo.findByProjectDetails(details);
		for (BaseDiverter bs : baseDiverter) {
			if (bs.getBdname().equals("DIVERTER TYPE")) {
				id = bs.getId();
			}
		}
		return id;
	}

	@Override
	public int dId(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> showbd(Integer pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> showtd(Integer pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseDiverter> changebd(Integer pid, String name) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<BaseDiverter> bdlist = new ArrayList<BaseDiverter>();
		if (baseDiverterRepo.findByProjectDetails(details) != null) {
			baseDiverterRepo.deleteByProjectDetails(details);
		}
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith(name)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						BaseDiverter bd = new BaseDiverter();
						String[] data = line.split("=");
						bd.setBdname(data[0]);
						bd.setBdvalue(data[1]);
						bd.setProjectDetails(details);
						bdlist.add(bd);
					}
					baseDiverterRepo.saveAll(bdlist);
					break;
				}

			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}

		return bdlist;
	}

	@Override
	public List<String> changetd(Integer pid, Integer bid, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDB(Integer pid, List<String> bdname, List<String> bdvalue) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<BaseDiverter> bdlist = baseDiverterRepo.findByProjectDetails(details);
		List<BaseDiverter> tempbdlist = new ArrayList<BaseDiverter>();

		for (int i = 0; i < bdlist.size(); i++) {
			BaseDiverter baseDiverter = bdlist.get(i);
			baseDiverter.setBdvalue(bdvalue.get(bdname.indexOf(bdlist.get(i).getBdname())));
			baseDiverter.setProjectDetails(details);
			tempbdlist.add(baseDiverter);
		}
		baseDiverterRepo.saveAll(tempbdlist);
	}

	@Override
	public String getDiverterType(Integer pid) {
		/********** RETURNING TYPE OF BASE DIVERTER **********/
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<BaseDiverter> bdlist = baseDiverterRepo.findByProjectDetails(details);
		if (bdlist.get(0).getBdvalue().equalsIgnoreCase("Viscosified")) {
			return "Viscosified";
		} else if (bdlist.get(0).getBdvalue().equalsIgnoreCase("Foamed")) {
			return "Foamed";
		} else if (bdlist.get(0).getBdvalue().equalsIgnoreCase("Particulate")) {
			return "Particulate";
		} else if (bdlist.get(0).getBdvalue().equalsIgnoreCase("Ves")) {
			return "Ves";
		}
		return null;
	}

	@Override
	public void saveTestDiverter(Integer pid, String td) {
		/***** ADDING TEST DIVERTER ******/
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<TestDiverter> bdlist = new ArrayList<TestDiverter>();
		try {
			File file = ResourceUtils.getFile("classpath:config/CarbonateDefaultData.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.equals(td)) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("/")) {
							break;
						}
						TestDiverter diverter = new TestDiverter();
						String[] data = line.split("=");
						diverter.setDname(data[0]);
						diverter.setDvalue(data[1]);
						diverter.setProjectDetails(details);
						diverter.setDtype(td);
						bdlist.add(diverter);
					}
					testDiverterRepo.saveAll(bdlist);
					break;
				}
			}
			br.close();
		} catch (Exception fne) {
			fne.printStackTrace();
		}

	}

	@Override
	public void removeTestDiverter(Integer pid, String td) {
		/***** REMOVING TEST DIVERTER ******/
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		if (testDiverterRepo.findByProjectDetails(details) != null) {
			testDiverterRepo.deleteByProjectDetailsAndDtype(details, td);
		}
	}

	@Override
	public List<String> getTestDirverterType(Integer pid) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<TestDiverter> bdlist = testDiverterRepo.findByProjectDetails(details);
		List<String> dtype = new ArrayList<String>();
		for (int i = 0; i < bdlist.size(); i++) {
			if (!dtype.contains(bdlist.get(i).getDtype())) {
				dtype.add(bdlist.get(i).getDtype());
			}
		}
		return dtype;
	}

	@Override
	public void updateTD(Integer pid, List<String> tdname, List<String> tdvalue, String td) {
		ProjectDetails details = projectDetailsRepository.findById(pid).orElse(null);
		List<TestDiverter> tdlist = testDiverterRepo.findByProjectDetailsAndDtype(details, td);
		List<TestDiverter> temp = new ArrayList<>();
		for (int i = 0; i < tdlist.size(); i++) {
			TestDiverter testDiverter = tdlist.get(i);
			testDiverter.setDvalue(tdvalue.get(tdname.indexOf(tdlist.get(i).getDname())));
			temp.add(testDiverter);
		}
		testDiverterRepo.saveAll(temp);
	}

}
