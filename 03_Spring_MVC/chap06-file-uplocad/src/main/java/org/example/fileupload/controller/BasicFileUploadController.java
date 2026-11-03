package org.example.fileupload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
* [Basic] 기본 파일 업로드 방식
* - 업로드 파일을 프로젝트 실행 위치의 uploads 폴더에 원본 파일명 그대로 저장
* - 요청 주소 : /basic-file-upload
* - 결과를 main.html에 바로 보여줌 (redirect 없이 model 사용)
* - 문제점 : 경로가 코드에 고정, 같은 파일명 덮어쓰기, 브라우저에서 조회 불가 (아래 주석 참고)
* */
@Controller
public class BasicFileUploadController {

    @GetMapping("/basic-file-upload")
    public String basicFileUploadPage() {

        return "redirect:/";
    }

    /*
    * MutipartFile
    * - spring 에서 업로드 파일을 다루는 인터페이스
    * - 파일명, 크기, 타입 내용을 확인 할 수 있다
    * - 저장을 위해서는 원하는 저장 경로로 옮겨야한다.
    *
    * */
    @PostMapping("/basic-file-upload")
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, Model model) throws IOException {

        // 경로
        //   Path uploadDirectory1 = Paths.get("uploads"); // upload
        //   Path uploadDirectory2 = Paths.get("uploads").toAbsolutePath(); // 절대경로
        //   Path uploadDirectory3 = Paths.get("uploads/./images/cat.jpg").normalize(); // 정리된 경로
        Path uploadDirectory = Paths.get("uploads").normalize(); // 정리된 경로

        // uploads 폴더를 생성한다. (있다면 아무작업이 일어나지 않음)
        Files.createDirectories(uploadDirectory);

        String originalfile = file.getOriginalFilename(); // 파일 이름 꺼냄
        System.out.println("originalfile = " + originalfile);

        // 경로와 파일명을 합침
        Path saveFile = uploadDirectory.resolve(originalfile);
        Files.copy(file.getInputStream(), saveFile);
        model.addAttribute("message", "파일 업로드성공!");
        model.addAttribute("originalFileName", originalfile);  // 원본 파일명
        model.addAttribute("savedPath", saveFile); // 지정 경로

        return "main";
    }

    /*
    * 1. 파일 경로가 JAVA 코드에 직접 작성되어 있다.
    *  문제 점=> 운영체제나 실행환경마다 다를때 코드를 수정해야하는 문제가 있다.
    *  해결 방벙 -> yaml에 DB별 경로를 설정해서 @Value("${upload.path}")로 주입하는 방식으로 해결
    *
    * 2. 같은 이름의 파일이 있을 경우 덮어쓰기가 일어난다.
    *  문제점 => 여러 사용자가 같은 파일명을 업로드하면 기존 파일이 삭제 될 수 있다.
    *  해결 방법 => UUID 기반의 고유한 저장 파일명을 생성
    *
    * 3. 저장한 파일을 브라우저에서 조회 불가하다.
    *  문제점 => 파일시스템 경로는 브라우저가 조회할수없다
    *  해결방법 =>  WebMvcConfigurer의 addResourceHandlers로 URL과 실제 저장 경로를 연결해서 보여준다
    * */

}
