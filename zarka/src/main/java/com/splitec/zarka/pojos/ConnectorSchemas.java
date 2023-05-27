package com.splitec.zarka.pojos;

public class ConnectorSchemas {

  public String schemaRequest;
  public String schemaResponse;
  public String errorMessage;

  public ConnectorSchemas(String responseString) {
    String[] values = responseString.split(";");

    this.schemaRequest = values[0];
    this.schemaResponse = values[1];
    this.errorMessage = values[2];
  }

  public ConnectorSchemas(){}

  public String getSchemaResponse() {
    return schemaResponse;
  }

  public String getSchemaRequest() {
    return schemaRequest;
  }

  public void setSchemaResponse(String schemaResponse) {
    this.schemaResponse = schemaResponse;
  }

  public void setSchemaRequest(String schemaRequest) {
    this.schemaRequest = schemaRequest;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
