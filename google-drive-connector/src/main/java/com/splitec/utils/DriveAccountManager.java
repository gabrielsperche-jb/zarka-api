package com.splitec.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.splitec.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class DriveAccountManager {

  private static final String APPLICATION_NAME = "connector";
  private static final String[] SCOPES = {DriveScopes.DRIVE};
  private static final String CloudVisionScope = "https://www.googleapis.com/auth/cloud-platform";

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  public static Drive getDriveService() throws IOException, GeneralSecurityException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new Drive.Builder(httpTransport, JSON_FACTORY, getCredential())
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  public static GoogleCredential getCredential() throws IOException {
    byte[] credsBytes = Constants.CREDENTIALS.getBytes(StandardCharsets.UTF_8);
    InputStream inputStream = new ByteArrayInputStream(credsBytes);
    return GoogleCredential.fromStream(inputStream)
        .createScoped(Arrays.asList(DriveScopes.DRIVE));
  }

  public static GoogleCredentials getVisionCredential() throws IOException {
    byte[] credsBytes = Constants.CREDENTIALS.getBytes(StandardCharsets.UTF_8);
    InputStream inputStream = new ByteArrayInputStream(credsBytes);
    return GoogleCredentials.fromStream(inputStream)
        .createScoped(Arrays.asList(CloudVisionScope));
  }
}
