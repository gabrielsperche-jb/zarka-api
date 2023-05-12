package com.splitec.zarka.domain;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.splitec.zarka.pojos.User;
import com.splitec.zarka.pojos.UserResponse;
import com.splitec.zarka.utils.JWTUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class UserExecutor {
  MongoClient mongoClient = new MongoClient("localhost", 27017);
  MongoDatabase database = mongoClient.getDatabase("zarka");
  MongoCollection<Document> collection = database.getCollection("users");

  public UserResponse buildUserDocument(JSONObject userInfo) {
    UserResponse response;
    try {
      String username = userInfo.getString("username");

      // Verificar se o usuário já existe antes de criar
      if (userExists(username)) {
        response = new UserResponse("", "Usuário já existe", "");
      } else {
        Document userDoc = new Document();
        userDoc.append("usuario", username);
        userDoc.append("senha", userInfo.getString("password"));

        // Inserir o documento e obter o ID gerado pelo MongoDB
        collection.insertOne(userDoc);
        String userId = userDoc.get("_id").toString();
        String token = JWTUtils.createToken(userId);

        response = new UserResponse(userId, "Cadastrado com sucesso", token);
      }
    } catch (Exception e) {
      response = new UserResponse("", e.getLocalizedMessage(), "");
    }
    return response;
  }

  private boolean userExists(String username) {
    Document query = new Document("usuario", username);
    return collection.countDocuments(query) > 0;
  }

  public boolean validateLogin(String username, String password) {
    Document query = new Document("usuario", username).append("senha", password);
    return collection.countDocuments(query) > 0;
  }

  public Document findUserByUsername(String username) {
    Document query = new Document("usuario", username);
    return collection.find(query).first();
  }

  public User findUserById(String userId) {
    try {
      ObjectId objectId = new ObjectId(userId);
      Bson filter = Filters.eq("_id", objectId);

      Document userDoc = collection.find(filter).first();

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
