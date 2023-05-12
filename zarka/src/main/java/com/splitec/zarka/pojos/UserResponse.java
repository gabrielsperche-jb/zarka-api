package com.splitec.zarka.pojos;

public class UserResponse {
  private String userId;
  private String message;
  private String token;

  public UserResponse(String userId, String message, String token) {
    this.userId = userId;
    this.message = message;
    this.token = token;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
