package com.petroleumsoft.stimopti.services.implementation;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.petroleumsoft.stimopti.modal.ProjectDetails;
import com.petroleumsoft.stimopti.modal.ReservoirLithology;
import com.petroleumsoft.stimopti.repo.ProjectDetailsRepository;
import com.petroleumsoft.stimopti.repo.ReservoirLithologyRepo;
import com.petroleumsoft.stimopti.services.ReadExcel;

@Service("readExcel")
public class ReadExcelImplementation implements ReadExcel {

	@Autowired
	ProjectDetailsRepository projectDetailsRepository;

	@Autowired
	ReservoirLithologyRepo reservoirLithologyRepo;

	@Override
	public List<ReservoirLithology> saveExcel(MultipartFile file, Integer id) throws Exception {
		ProjectDetails details = projectDetailsRepository.findById(id).orElse(null);
		List<String> rlist = new ArrayList<String>();
		ReservoirLithology reservoirLithology = null;
		List<ReservoirLithology> savelist = new ArrayList<ReservoirLithology>();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		for(int j=2;j<worksheet.getPhysicalNumberOfRows();j++) {
			Row cell=worksheet.getRow(j);
			reservoirLithology = new ReservoirLithology();
			for (int i = 1; i < cell.getPhysicalNumberOfCells(); i++) {
				String value = "";
				if (cell.getCell(i).getCellTypeEnum() == CellType.STRING) {
					value = cell.getCell(i).getStringCellValue();
				} else {
					value = Double.toString(cell.getCell(i).getNumericCellValue());
				}
				System.out.println(value);
				rlist.add(value);
			}
			// System.out.println(rlist);
			reservoirLithology.setFromDefthFT(rlist.get(0));
			reservoirLithology.setToDefthFT(rlist.get(1));
			reservoirLithology.settVDFT(rlist.get(2));
			reservoirLithology.setPermMD(rlist.get(3));
			reservoirLithology.setPoro(rlist.get(4));
			reservoirLithology.setZonePressPSI(rlist.get(5));
			reservoirLithology.setPrestimskin(rlist.get(6));
			reservoirLithology.setPrestimpi(rlist.get(7));
			reservoirLithology.setProjectDetails(details);
			savelist.add(reservoirLithology);
			workbook.close();
			rlist.clear();
		}
		reservoirLithologyRepo.saveAll(savelist);
		return savelist;
	}
}
