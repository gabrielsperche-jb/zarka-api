package com.splitec.zarka.controller;

import com.splitec.zarka.Constants;
import com.splitec.zarka.domain.UserExecutor;
import com.splitec.zarka.domain.WorkflowExecutor;
import com.splitec.zarka.pojos.ConnectorResponse;
import com.splitec.zarka.pojos.ConnectorSchemas;
import com.splitec.zarka.pojos.User;
import com.splitec.zarka.pojos.UserCredentials;
import com.splitec.zarka.pojos.UserResponse;
import com.splitec.zarka.utils.JWTUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @CrossOrigin(origins = "https://zarkaweb.herokuapp.com")
  @PostMapping(value = "/workflow/run")
  public ResponseEntity<Object> runWorkflow(@RequestBody String body, @RequestHeader(AUTHORIZATION) String token) {
    ConnectorResponse error = new ConnectorResponse();
    try {
      if (JWTUtils.validateToken(token)) {
        JSONArray workflowConfig = new JSONArray(body);
        ConnectorResponse result = executor.executeFlow(workflowConfig);
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
      error.setErrorMessage("Invalid Token");
      error.setSuccess(false);
      return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      error.setErrorMessage(e.getLocalizedMessage());
      error.setSuccess(false);
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }

  @CrossOrigin(origins = "https://zarkaweb.herokuapp.com/")
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

  @CrossOrigin(origins = "https://zarkaweb.herokuapp.com/")
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

  @CrossOrigin(origins = "https://zarkaweb.herokuapp.com/")
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

  @CrossOrigin(origins = "https://zarkaweb.herokuapp.com/")
  @GetMapping(value = "/connector/schema/{connectorName}/{requestName}")
  public ResponseEntity<Object> getSchemas(@PathVariable String connectorName,
                                           @PathVariable String requestName,
                                           @RequestHeader(AUTHORIZATION) String token) {
    ConnectorResponse error = new ConnectorResponse();
    try {
      if (JWTUtils.validateToken(token)) {
        ConnectorSchemas response = executor.getSchemas(connectorName, requestName);
        return new ResponseEntity<>(response, HttpStatus.OK);
      }
      error.setErrorMessage("Invalid Token");
      error.setSuccess(false);
      return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      error.setErrorMessage(e.getLocalizedMessage());
      error.setSuccess(false);
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }
}