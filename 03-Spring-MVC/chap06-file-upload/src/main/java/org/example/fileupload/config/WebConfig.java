package org.example.fileupload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* WebMvcConfigurer
* - Spring MVC의 기본 설정을 유지하면서 필요한 기능만 추가하는 인터페이스
* - 정적 리소스, CORS, 인터셉터 등 웹 설정을 재정의 가능
* */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.windows.path}") // 실행시키는 OS에 따라 경로 설정
    private String windowsPath;

    @Value("${file.upload.mac.path}")
    private String macPath;

    /*
    * addResourceHandler
    * - 정적파일의 접근을 관리할 수 있는 메서드
    * - 브라우저가 요청할 URL과 서버의 실제 파일 경로를 연결(매핑)
    * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String osName = System.getProperty("os.name").toLowerCase();
        String uploadPath;

        if( osName.contains("win")) {
            uploadPath = windowsPath;
        } else {
            uploadPath = macPath;
        }

        System.out.println("현재 내 OS = " + osName);
        System.out.println("업로드 파일경로 = " + uploadPath);

        // 실제 업로드된 파일에 접근할 URL 패턴과
        // 실제 파일 시스템의 경로 매핑
        // "file:///C:/Users/이영현/04_SpringBoot/03-Spring-MVC/chap06-file-upload/uploadedFiles/img/single/" + savedName
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///"+uploadPath +"/");
    }
}
