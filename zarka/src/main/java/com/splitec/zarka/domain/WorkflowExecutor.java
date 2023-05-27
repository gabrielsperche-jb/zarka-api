package com.splitec.zarka.domain;

import com.splitec.zarka.pojos.ConnectorResponse;
import com.splitec.zarka.pojos.ConnectorSchemas;
import com.splitec.zarka.repository.WorkflowRepository;
import com.splitec.zarka.utils.JWTUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class WorkflowExecutor {
  WorkflowRepository repository = new WorkflowRepository();

  public ConnectorResponse executeFlow(JSONArray config) throws ClassNotFoundException, InvocationTargetException,
      InstantiationException, IllegalAccessException, NoSuchMethodException {
    try {
      ConnectorResponse connectorResponse = null;
      int index = 0;
      for (Object obj : config) {
        JSONObject connectorConfig;
        if (obj instanceof JSONObject) {
          connectorConfig = (JSONObject) obj;
        } else {
          connectorConfig = new JSONObject(obj.toString());
        }
        if (index > 0) {
          if (connectorResponse.isSuccess()) {
            connectorConfig.put("lastRequestResponse", connectorResponse.getResponse());
          } else {
            throw new Exception("Last request has ended with error: " + connectorResponse.getErrorMessage());
          }

        }
        connectorResponse = repository.callConnector(connectorConfig.getString("connectorName"), connectorConfig);
        index++;
      }
      return connectorResponse;
    } catch (Exception e) {
      ConnectorResponse errorResponse = new ConnectorResponse();
      errorResponse.setErrorMessage(e.getLocalizedMessage());
      errorResponse.setSuccess(false);
      return errorResponse;
    }
  }

  public String createToken(JSONObject config) {
    return JWTUtils.createToken(config.getString("userId"));
  }

  public ConnectorSchemas getSchemas(String connectorName, String requestName) {
    return repository.getSchemasFromConnector(connectorName, requestName);
  }
}
