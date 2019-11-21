package com.petroleumsoft.stimopti.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.petroleumsoft.stimopti.modal.ReservoirLithology;

public interface ReadExcel {
public List<ReservoirLithology>saveExcel(MultipartFile file,Integer id) throws Exception;
}
