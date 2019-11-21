package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.Penetration;
import com.petroleumsoft.stimopti.modal.Placement;
import com.petroleumsoft.stimopti.modal.Productivity;
import com.petroleumsoft.stimopti.modal.Skin;

public interface ChartsService {
public List<Penetration> penetration(Integer pid);
public List<Placement> placement(Integer pid);
public List<Skin> skin(Integer pid);
public List<Productivity> pi(Integer pid);

}
