package com.splitec.service;

import com.splitec.pojos.ConnectorResponse;
import com.splitec.pojos.ConnectorSchemas;
import org.json.JSONObject;
import com.splitec.domain.ChatGPTDomain;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChatGPTService {

  ChatGPTDomain domain = new ChatGPTDomain();

  public String execute(JSONObject config) throws InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {
    switch (config.getString("activity")) {
      case "retrieve":
        String requestName = config.getString("requestName");
        JSONObject body = config.getJSONObject("requestBody");
        if (config.has("lastRequestResponse")) {
          body.put("lastRequestResponse", config.getString("lastRequestResponse"));
        }
        return domain.retrieveActivity(requestName, body).toString();
    }
    ConnectorResponse errorResponse = new ConnectorResponse();
    errorResponse.setErrorMessage("Activity not implement yet");
    return errorResponse.toString();
  }

  public String provideSchemas(String methodName) throws IOException {
    ConnectorSchemas schemas = new ConnectorSchemas();
    String requestFile = "chatGPT-connector/src/main/resources/schema/" + methodName + "_request.json";
    String responseFile = "chatGPT-connector/src/main/resources/schema/" + methodName + "_response.json";
    String requestSchema;
    String responseSchema;
    try {
      requestSchema = new String(Files.readAllBytes(Paths.get(requestFile)));
      responseSchema = new String(Files.readAllBytes(Paths.get(responseFile)));
      schemas.setSchemaRequest(requestSchema);
      schemas.setSchemaResponse(responseSchema);
      return schemas.toString();
    } catch (IOException e) {
      schemas.setErrorMessage(e.getLocalizedMessage());
      return schemas.toString();
    }
  }

}
