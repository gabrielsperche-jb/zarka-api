package com.splitec.domain;

import com.splitec.repository.DriveRepository;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DriveDomain {
  DriveRepository repository = new DriveRepository();

  public String retrieveActivity(String requestName, JSONObject requestBody) throws InstantiationException, IllegalAccessException,
      NoSuchMethodException, InvocationTargetException {
    Class<? extends DriveRepository> cls = repository.getClass();
    Object obj = cls.newInstance();
    Method method = cls.getMethod(requestName, String.class);
    return (String) method.invoke(obj, getFolderName(requestBody));
  }

  private String getFolderName(JSONObject body) {
    return body.getString("folderName");
  }

}
