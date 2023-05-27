package com.splitec.pojos;

public class ConnectorResponse {
  private String response;
  private boolean success;
  private String errorMessage;

  public ConnectorResponse(String response, boolean success, String errorMessage) {
    this.response = response;
    this.success = success;
    this.errorMessage = errorMessage;
  }

  public ConnectorResponse() {
  }


  public String getResponse() {
    return response;
  }

  public ConnectorResponse(String responseString) {
    String[] values = responseString.split(";");

    this.response = values[0];
    this.success = Boolean.parseBoolean(values[1]);
    this.errorMessage = values[2];
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  @Override
  public String toString() {
    return response + ";" + success + ";" + errorMessage;
  }
}
