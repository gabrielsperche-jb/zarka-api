package com.splitec.service;

import com.splitec.domain.DriveDomain;
import com.splitec.pojos.ConnectorResponse;
import com.splitec.pojos.ConnectorSchemas;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DriveService {

  DriveDomain domain = new DriveDomain();

  public String execute(JSONObject config) throws InvocationTargetException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {
    switch (config.getString("activity")) {
      case "retrieve":
        return domain.retrieveActivity(config.getString("requestName"), config.getJSONObject("requestBody")).toString();
    }
    ConnectorResponse errorResponse = new ConnectorResponse();
    errorResponse.setErrorMessage("Activity not implement yet");
    return errorResponse.toString();
  }

  public String provideSchemas(String methodName) throws IOException {
    ConnectorSchemas schemas = new ConnectorSchemas();
    String requestFile = "google-drive-connector/src/main/resources/schema/" + methodName + "_request.json";
    String responseFile = "google-drive-connector/src/main/resources/schema/" + methodName + "_response.json";
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
