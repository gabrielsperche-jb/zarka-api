package com.splitec.zarka.repository;

import com.splitec.zarka.Constants;
import com.splitec.zarka.pojos.ConnectorResponse;
import com.splitec.zarka.pojos.ConnectorSchemas;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class WorkflowRepository implements Constants {

  public ConnectorResponse callConnector(String connectorName, JSONObject connectorConfig) {
    try {
      Class<?> cls = Class.forName("com.splitec.service." + connectorName + "Service");
      Object obj = cls.newInstance();
      Method method = cls.getMethod(EXECUTE_METHOD, JSONObject.class);
      String response = (String) method.invoke(obj, connectorConfig);
      return new ConnectorResponse(response);
    } catch (Exception e) {
      ConnectorResponse errorResponse = new ConnectorResponse();
      errorResponse.setErrorMessage(e.getLocalizedMessage());
      errorResponse.setSuccess(false);
      return errorResponse;
    }
  }

  public ConnectorSchemas getSchemasFromConnector(String connectorName, String requestName) {
    try {
      Class<?> cls = Class.forName("com.splitec.service." + connectorName + "Service");
      Object obj = cls.newInstance();
      Method method = cls.getMethod(SCHEMA_METHOD, String.class);
      String response = (String) method.invoke(obj, requestName);
      return new ConnectorSchemas(response);
    } catch (Exception e) {
      ConnectorSchemas errorResponse = new ConnectorSchemas();
      errorResponse.setErrorMessage(e.getLocalizedMessage());
      return errorResponse;
    }
  }
}
