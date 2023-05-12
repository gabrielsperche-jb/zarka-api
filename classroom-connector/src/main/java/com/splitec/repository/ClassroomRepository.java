package com.splitec.repository;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.splitec.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class ClassroomRepository {
  public String listCourses() {
    String apiResponse = null;
    try {
      Classroom classroom = createConnection();
      ListCoursesResponse response = classroom.courses().list().execute();
      List<Course> courses = response.getCourses();
      apiResponse = courses.get(0).toPrettyString();
    } catch (Exception e) {
      return e.getLocalizedMessage();
    }
    return apiResponse;
  }

  private Classroom createConnection() throws GeneralSecurityException, IOException {
    String APPLICATION_NAME = "Connector";
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    try {
      byte[] credsBytes = Constants.CREDENTIALS.getBytes(StandardCharsets.UTF_8);
      InputStream inputStream = new ByteArrayInputStream(credsBytes);
      GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
          .createScoped(Arrays.asList(ClassroomScopes.CLASSROOM_COURSES, ClassroomScopes.CLASSROOM_ROSTERS));

      HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
      return new Classroom.Builder(httpTransport, jsonFactory, requestInitializer)
          .setApplicationName(APPLICATION_NAME)
          .build();
    } catch (Exception e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
  }
}
