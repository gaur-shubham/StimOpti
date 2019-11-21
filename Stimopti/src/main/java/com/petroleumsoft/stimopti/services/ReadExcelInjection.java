package com.petroleumsoft.stimopti.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.petroleumsoft.stimopti.modal.*;

public interface ReadExcelInjection {
public List<InjectionPlan>saveExcelinj(MultipartFile file,Integer id) throws Exception;
}
