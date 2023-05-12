package com.splitec.zarka.domain;

import com.splitec.zarka.repository.WorkflowRepository;
import com.splitec.zarka.utils.JWTUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class WorkflowExecutor {
  WorkflowRepository repository = new WorkflowRepository();

  public String executeFlow(JSONArray config) throws ClassNotFoundException, InvocationTargetException,
      InstantiationException, IllegalAccessException, NoSuchMethodException {
    String connectorResponse = "";
    for (Object obj : config) {
      JSONObject connectorConfig;
      if (obj instanceof JSONObject) {
        connectorConfig = (JSONObject) obj;
      } else {
        connectorConfig = new JSONObject(obj.toString());
      }
      connectorResponse = repository.callConnector(connectorConfig.getString("connectorName"), connectorConfig).toString();
    }
    return connectorResponse;
  }

  public String createToken(JSONObject config) {
    return JWTUtils.createToken(config.getString("userId"));
  }
}
