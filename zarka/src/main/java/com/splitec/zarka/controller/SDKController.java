package com.splitec.zarka.controller;

import com.splitec.zarka.Constants;
import com.splitec.zarka.domain.UserExecutor;
import com.splitec.zarka.domain.WorkflowExecutor;
import com.splitec.zarka.pojos.User;
import com.splitec.zarka.pojos.UserCredentials;
import com.splitec.zarka.pojos.UserResponse;
import com.splitec.zarka.utils.JWTUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sdk")
public class SDKController implements Constants {
  WorkflowExecutor executor = new WorkflowExecutor();
  @Autowired
  private UserExecutor userExecutor;

  @PostMapping(value = "/workflow/run")
  public ResponseEntity<Object> runWorkflow(@RequestBody String body, @RequestHeader(AUTHORIZATION) String token) {
    try {
      if (JWTUtils.validateToken(token)) {
        JSONArray workflowConfig = new JSONArray(body);
        String result = executor.executeFlow(workflowConfig);
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
      return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/workflow/token")
  public ResponseEntity<Object> createToken(@RequestBody String body) {
    try {
      JSONObject tokenReq = new JSONObject(body);
      String result = executor.createToken(tokenReq);
      return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    } catch (Exception e) {
      return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/user/create")
  public ResponseEntity<Object> saveUser(@RequestBody String body) {
    try {
      JSONObject userInfo = new JSONObject(body);
      UserResponse result = userExecutor.buildUserDocument(userInfo);
      return new ResponseEntity<>(result, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(new UserResponse("", "Error while processing request", ""),
          HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/user/login")
  public ResponseEntity<Object> login(@RequestBody UserCredentials credentials, @RequestHeader(AUTHORIZATION) String token) {
    try {
      String userIdFromToken = JWTUtils.getUserIdFromToken(token);
      User user = userExecutor.findUserById(userIdFromToken);

      if (user != null && JWTUtils.validateToken(token) && user.getUsername().equals(credentials.getUsername())) {
        if (userExecutor.validateLogin(credentials.getUsername(), credentials.getPassword())) {
          return new ResponseEntity<>(new UserResponse("", "Success", ""), HttpStatus.OK);
        }
      }
      return new ResponseEntity<>(new UserResponse("", "Invalid User or Token", ""), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return new ResponseEntity<>(new UserResponse("", "Error while processing request", ""), HttpStatus.BAD_REQUEST);
    }
  }

}