package com.petroleumsoft.stimopti.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.petroleumsoft.stimopti.modal.InjectionPlan;
import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.repo.InjectionPlanRepo;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.services.ReadExcelInjection;
@Service("readExcelInjection")
public class ReadExcelInjectionImplementation implements ReadExcelInjection {
	@Autowired
     ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	InjectionPlanRepo injectionPlanRepo;
	@Override
	public List<InjectionPlan> saveExcelinj(MultipartFile file, Integer id) throws Exception {
		
		System.out.println("I am in readInjection ");
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		List<String> injlist = new ArrayList<String>();
		InjectionPlan injectionPlan = null;
		List<InjectionPlan> savelist = new ArrayList<InjectionPlan>();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		for(int j=2;j<worksheet.getPhysicalNumberOfRows();j++) {
			Row row = worksheet.getRow(j);
			injectionPlan = new InjectionPlan();
			for (int i = 1; i < row.getPhysicalNumberOfCells(); i++) {
				String value = "";
				if (row.getCell(i).getCellTypeEnum() == CellType.STRING) {
					value = row.getCell(i).getStringCellValue();
				} else {
					value = Double.toString(row.getCell(i).getNumericCellValue());
				}
				injlist.add(value);
			}
			injectionPlan.setDuration(injlist.get(0));;
			injectionPlan.setStage(injlist.get(1));
			injectionPlan.setStavageVolBbls(injlist.get(2));
			injectionPlan.setPumpRateBMP(injlist.get(3));
			injectionPlan.setInjPressPSI(injlist.get(4));
			injectionPlan.setBhppsi(injlist.get(5));
		    injectionPlan.setDirection(injlist.get(6));
			injectionPlan.setInjDefthFT(injlist.get(7));
			injectionPlan.setProjectDetails(details);
			savelist.add(injectionPlan);
			workbook.close();
			injlist.clear();
		}
		injectionPlanRepo.saveAll(savelist);
		return savelist;
	}

}
