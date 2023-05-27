package com.splitec.repository;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;
import com.splitec.pojos.ConnectorResponse;
import com.splitec.utils.DriveAccountManager;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class DriveRepository {

  private static final String IMAGE_MIME_TYPE = "image/jpeg";

  public ConnectorResponse getFileContentFromFolder(String folderName) throws IOException, GeneralSecurityException {
    Drive driveService = DriveAccountManager.getDriveService();

    String query = "mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'";
    FileList result = driveService.files().list().setQ(query).setFields("files(id)").execute();
    List<File> folders = result.getFiles();
    if (folders.isEmpty()) {
      throw new FileNotFoundException("Não foi possível encontrar a pasta com o nome " + folderName);
    }
    String folderId = folders.get(0).getId();
    query = "'" + folderId + "' in parents";
    result = driveService.files().list().setQ(query).setFields("nextPageToken, files(id, name, mimeType)").execute();
    List<File> files = result.getFiles();

    String extractedText;
    String finalText = "";
    ConnectorResponse response = new ConnectorResponse();
    for (File file : files) {
      if (IMAGE_MIME_TYPE.equals(file.getMimeType())) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
        byte[] imageData = outputStream.toByteArray();
        extractedText = extractTextFromImage(imageData, DriveAccountManager.getVisionCredential()) + " ";
        finalText = finalText.concat(extractedText);
      }
    }
    JSONObject objResponse = new JSONObject();
    objResponse.put("imageContent", finalText);
    response.setResponse(objResponse.toString());
    response.setSuccess(true);
    return response;
  }

  public String extractTextFromImage(byte[] imageData, GoogleCredentials credentials) throws IOException {
    ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
        .build();

    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {
      Image image = Image.newBuilder().setContent(ByteString.copyFrom(imageData)).build();
      Feature feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
      AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
      List<AnnotateImageRequest> requests = new ArrayList<>();
      requests.add(request);

      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      if (responses.isEmpty()) {
        return "";
      }

      // Retorna o texto extraído da imagem
      return responses.get(0).getFullTextAnnotation().getText();
    }
  }


}
