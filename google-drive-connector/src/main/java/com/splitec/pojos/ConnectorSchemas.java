package com.splitec.pojos;

public class ConnectorSchemas {

  public String schemaRequest;
  public String schemaResponse;
  public String errorMessage;

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

  @Override
  public String toString(){
    return schemaRequest + ";" + schemaResponse + ";" + errorMessage;
  }
}
