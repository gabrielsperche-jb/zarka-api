package com.splitec.zarka.repository;

import com.splitec.zarka.Constants;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WorkflowRepository implements Constants {

  public Object callConnector(String connectorName, JSONObject connectorConfig) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Class<?> cls = Class.forName("com.splitec.service." + connectorName + "Service");
    Object obj = cls.newInstance();
    Method method = cls.getMethod(EXECUTE_METHOD, JSONObject.class);
    return method.invoke(obj, connectorConfig);
  }
}
