package com.splitec.zarka.domain;

import com.mongodb.BasicDBObject;
import com.splitec.zarka.pojos.User;
import com.splitec.zarka.pojos.UserResponse;
import com.splitec.zarka.utils.JWTUtils;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UserExecutor {

  @Autowired
  private MongoTemplate mongoTemplate;

  public UserResponse buildUserDocument(JSONObject userInfo) {
    UserResponse response;
    try {
      String username = userInfo.getString("username");
      if (userExists(username)) {
        response = new UserResponse("", "User exists already", "");
      } else {
        BasicDBObject userDoc = new BasicDBObject();
        userDoc.append("usuario", username);
        userDoc.append("senha", userInfo.getString("password"));

        mongoTemplate.insert(userDoc, "users");
        String userId = userDoc.get("_id").toString();
        String token = JWTUtils.createToken(userId);

        response = new UserResponse(userId, "User registered successfully", token);
      }
    } catch (Exception e) {
      response = new UserResponse("", e.getLocalizedMessage(), "");
    }
    return response;
  }

  private boolean userExists(String username) {
    Query query = new Query();
    query.addCriteria(Criteria.where("usuario").is(username));
    return mongoTemplate.count(query, "users") > 0;
  }

  public boolean validateLogin(String username, String password) {
    Query query = new Query();
    query.addCriteria(Criteria.where("usuario").is(username).and("senha").is(password));
    return mongoTemplate.count(query, "users") > 0;
  }

  public Document findUserByUsername(String username) {
    Query query = new Query();
    query.addCriteria(Criteria.where("usuario").is(username));
    return mongoTemplate.findOne(query, Document.class, "users");
  }

  public User findUserById(String userId) {
    try {
      Query query = new Query();
      query.addCriteria(Criteria.where("_id").is(userId));
      Document userDoc = mongoTemplate.findOne(query, Document.class, "users");

      if (userDoc != null) {
        String username = userDoc.getString("usuario");
        return new User("", username);
      } else {
        return null;
      }
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

}
