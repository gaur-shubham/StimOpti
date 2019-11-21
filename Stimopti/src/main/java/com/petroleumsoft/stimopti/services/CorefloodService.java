package com.petroleumsoft.stimopti.services;

import java.util.List;

import com.petroleumsoft.stimopti.modal.Coreflood;

public interface CorefloodService {
public List<Coreflood> changeType(Integer pid,String cv);
public List<Coreflood> setNotAvailable(Integer pid);

}
