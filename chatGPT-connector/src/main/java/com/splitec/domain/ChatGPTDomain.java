package com.splitec.domain;

import com.splitec.pojos.ConnectorResponse;
import com.splitec.repository.ChatGPTRepository;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ChatGPTDomain {

  ChatGPTRepository repository = new ChatGPTRepository();

  public ConnectorResponse retrieveActivity(String requestName, JSONObject requestBody) throws InstantiationException, IllegalAccessException,
      NoSuchMethodException, InvocationTargetException {
    try {
      if (requestBody.has("lastRequestResponse")) {
        String lastResp = requestBody.getString("lastRequestResponse");
        handleLastRequest(new JSONObject(lastResp), requestBody);
      }
      Class<? extends ChatGPTRepository> cls = repository.getClass();
      Object obj = cls.newInstance();
      Method method = cls.getMethod(requestName, JSONObject.class);
      return (ConnectorResponse) method.invoke(obj, requestBody);
    } catch (Exception e) {
      ConnectorResponse errorResponse = new ConnectorResponse();
      errorResponse.setErrorMessage(e.getLocalizedMessage());
      errorResponse.setSuccess(false);
      return errorResponse;
    }

  }

  public void handleLastRequest(JSONObject lastResponse, JSONObject requestBody) {
    String newContent = null;
    String removedKey = null;
    String actualValue;
    for (String key : requestBody.keySet()) {
      actualValue = requestBody.getString(key);
      if (actualValue.startsWith("$")) {
        newContent = lastResponse.getString(actualValue.substring(1));
        removedKey = key;
      }
    }
    if (newContent != null && removedKey != null) {
      requestBody.remove(removedKey);
      requestBody.put(removedKey, newContent);
    }
  }
}
