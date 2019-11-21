package com.petroleumsoft.stimopti.services;

import com.petroleumsoft.stimopti.modal.ProjectDetails;

public interface GenerateInputService {
public void generateInput(Integer id);
public void generateOuput(String pumpingType,ProjectDetails details);
public void fillData();
}
