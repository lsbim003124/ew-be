package com.lsbim.wowlsb.gcp.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@SpringBootTest
@Log4j2
public class GCPStorageTests {

 /*   private Storage storage;

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

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    String imageUrl = "https://assets2.rpglogs.com/img/warcraft/abilities/inv_arathilynxmount_black.jpg";
    int fileNumber = 447439;*/

  /*  @Test
    public void GCPStorageTest1()  throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();

        // 2. Google Cloud Storage 클라이언트 초기화
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();


        // 3. 이미지 URL에 연결
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // 일부 서버에서는 User-Agent가 필요함
        InputStream imageStream = connection.getInputStream();

        // 4. 제공된 숫자로 사용자 지정 파일 이름 생성
        String fileName = "ability/" + fileNumber + ".jpg";

        // 5. 사용자 지정 경로/파일 이름 및 콘텐츠 유형으로 BlobInfo 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(connection.getContentType())
                .build();

        // 6. 이미지 스트림을 ability/ 폴더에 있는 GCS 버킷에 업로드
        Blob blob = storage.create(blobInfo, imageStream);
        log.info("Uploaded file {} to bucket {}", fileName, bucketName);

        // 7. 모든 스트림 닫기
        imageStream.close();
        keyFile.close();
    }*/

    /*@Test
    public void GCPStorageTest2()  throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        String contentType = connection.getContentType();

        try (InputStream imageStream = connection.getInputStream()) { // 이미지 인풋스트림에 try-with-resources 패턴 적용
            String fileName = "images/ability/" + fileNumber + ".jpg";

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(contentType)
                    .build();

            Blob blob = storage.createFrom(blobInfo, imageStream);
            log.info("Uploaded file {} to bucket {}", fileName, bucketName);
        }
    }*/
}
