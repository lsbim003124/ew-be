package com.lsbim.wowlsb.service.gcp.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Service
@Log4j2
public class GCSService {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    // 클래스 변수로 Storage 선언
    private Storage storage;

    private final String imgUrl = "https://assets2.rpglogs.com/img/warcraft/abilities/";

    // 빈 초기화 시 한 번만 Storage 객체 생성
    @PostConstruct
    public void initStorage() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
        storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();
        keyFile.close();
    }

    public void uploadObject(String icon, int spellId) {
        try {
            URL url = new URL(imgUrl + icon);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            String contentType = connection.getContentType();

            // 이미지 인풋스트림에 try-with-resources 패턴 적용
            // try 블록이 닫힐때마다 imageStream을 닫는 것이 주 목적
            try (InputStream imageStream = connection.getInputStream()) {
                String fileName = "images/ability/" + spellId + ".jpg";

                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                        .setContentType(contentType)
                        .build();

                Blob blob = storage.createFrom(blobInfo, imageStream);
                log.info("Uploaded file {} to bucket {}", fileName, bucketName);
            } catch (IOException e) {
                log.error("Failed Upload Image to GCS... icon: {}, spellId: {}", icon, spellId, e);
            }
        } catch (IOException e) {
            log.error("Failed Connect to GCS", e);
        } catch (Exception e){
            log.error("Failed Connect to GCS", e);
        }

    }
}
