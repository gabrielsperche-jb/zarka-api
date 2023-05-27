package com.splitec.domain;

import com.splitec.pojos.ConnectorResponse;
import com.splitec.repository.DriveRepository;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DriveDomain {
  DriveRepository repository = new DriveRepository();

  public ConnectorResponse retrieveActivity(String requestName, JSONObject requestBody) throws InstantiationException, IllegalAccessException,
      NoSuchMethodException, InvocationTargetException {
    try {
      Class<? extends DriveRepository> cls = repository.getClass();
      Object obj = cls.newInstance();
      Method method = cls.getMethod(requestName, String.class);
      return (ConnectorResponse) method.invoke(obj, getFolderName(requestBody));
    } catch (Exception e) {
      ConnectorResponse errorResponse = new ConnectorResponse();
      errorResponse.setErrorMessage(e.getLocalizedMessage());
      errorResponse.setSuccess(false);
      return errorResponse;
    }

  }

  private String getFolderName(JSONObject body) {
    return body.getString("folderName");
  }

}
