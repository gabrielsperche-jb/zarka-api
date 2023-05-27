package com.splitec.repository;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.splitec.Constants;
import com.splitec.pojos.ConnectorResponse;
import org.json.JSONObject;

public class ChatGPTRepository {

  ConnectorResponse connectorResponse = new ConnectorResponse();

  public ConnectorResponse sendPrompt(JSONObject requestBody) {

    String content = requestBody.getString("text");
    String type = requestBody.getString("type");
    String prompt;
    if (type.equalsIgnoreCase(Constants.STUDY_TYPE)) {
      prompt = Constants.STUDY + content;
    } else {
      prompt = Constants.TOPIC + content;
    }

    JSONObject body = new JSONObject();
    body.put("prompt", prompt);
    body.put("max_tokens", 1000);
    body.put("model", "text-davinci-003");
    body.put("temperature", 0);

    try {
      HttpResponse<JsonNode> response = Unirest.post("https://api.openai.com/v1/completions")
          .header("Content-Type", "application/json")
          .header("Authorization", Constants.TOKEN)
          .body(body)
          .asJson();


      JSONObject objResponse = new JSONObject();
      objResponse.put("content", response.getBody().getObject()
          .getJSONArray("choices").getJSONObject(0).getString("text"));
      connectorResponse.setResponse(objResponse.toString());
      connectorResponse.setSuccess(true);

      return connectorResponse;
    } catch (UnirestException e) {
      connectorResponse.setErrorMessage(e.getLocalizedMessage());
      connectorResponse.setSuccess(false);
      return connectorResponse;
    }
  }
}
