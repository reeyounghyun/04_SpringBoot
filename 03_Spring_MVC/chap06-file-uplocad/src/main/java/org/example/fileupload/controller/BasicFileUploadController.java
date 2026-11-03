package org.example.fileupload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, Model model) {

        // 경로
        //   Path uploadDirectory1 = Paths.get("uploads"); // upload
        //   Path uploadDirectory2 = Paths.get("uploads").toAbsolutePath(); // 절대경로
        //   Path uploadDirectory3 = Paths.get("uploads/./images/cat.jpg").normalize(); // 정리된 경로
        Path uploadDirectory = Paths.get("uploads").normalize(); // 정리된 경로

        return "main";
    }

}
