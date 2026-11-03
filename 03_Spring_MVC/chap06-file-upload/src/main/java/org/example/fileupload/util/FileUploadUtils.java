package org.example.fileupload.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileUploadUtils {

    /*
    * @Value
    * - application.yaml 등의 설정값을 변수에 주입하는 어노테이션
    * - 설정파일의 해당 경로값을 가져와서 주입해준다.
    * */
    @Value("${file.upload.windows.path}") // 실행시키는 OS에 따라 경로 설정
    private String windowsPath;

    @Value("${file.upload.mac.path}")
    private String macPath;

    public String getUploadPath(String subdirectory) {
        String osName = System.getProperty("os.name").toLowerCase();
        String basePath;

        // OS에 따라 기본 경로 설정
        if(osName.contains("win")) {
            basePath = windowsPath;
        } else {
            basePath = macPath;
        }

        String uploadPath = basePath + "/" + subdirectory;
        File directory = new File(uploadPath); // 파일 객체 생성

        if (!directory.exists()) {  // 해당 파일이 있는지 확인
            System.out.println("파일 업로드를 위한 디렉토리 생성!");

            boolean created = directory.mkdirs(); // 경로에 맞게 디렉토리 생성

            if(!created) {
                throw new RuntimeException("파일 업로드 디렉토리 생성 실패 : " + uploadPath);
            }
        }

        return uploadPath;
    }
}
