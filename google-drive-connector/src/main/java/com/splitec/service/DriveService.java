package com.splitec.service;

import com.splitec.domain.DriveDomain;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class DriveService {

  DriveDomain domain = new DriveDomain();

  public String execute(JSONObject config) throws InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {
    switch (config.getString("activity")) {
      case "retrieve":
        return domain.retrieveActivity(config.getString("requestName"), config.getJSONObject("requestBody"));
    }
    return "Activity not implement yet";
  }
}
