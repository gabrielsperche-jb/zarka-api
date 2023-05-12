package com.splitec.service;

import com.splitec.domain.ClassroomDomain;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class ClassroomService {

  ClassroomDomain domain = new ClassroomDomain();

  public String execute(JSONObject config) throws InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {
    switch (config.getString("activity")) {
      case "retrieve":
        return domain.retrieveActivity(config.getString("requestName"));
    }
    return "Activity not implement yet";
  }
}
