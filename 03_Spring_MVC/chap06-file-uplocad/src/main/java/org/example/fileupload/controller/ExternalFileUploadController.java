package org.example.fileupload.controller;

import org.example.fileupload.util.FileUploadUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ExternalFileUploadController {

    private final FileUploadUtils fileUploadUtils;

    public ExternalFileUploadController(FileUploadUtils fileUploadUtils) {
        this.fileUploadUtils = fileUploadUtils;
    }

    @PostMapping("external-single-file")
    public String externalSingleFileUpload(
            @RequestParam MultipartFile singleFile,
            @RequestParam String singleFileDescription,
            RedirectAttributes redirectAttributes
    ) throws IOException {

        String filePath = fileUploadUtils.getUploadPath("img/single"); // yaml에 설정한 외부 경로 + img/single
        String originalFileName =  singleFile.getOriginalFilename();



        /*uuID생성
        *  - 파일명의 UUID를 만들어 반환해 저장
        * */
        String savedName = createSavedName(originalFileName);

        singleFile.transferTo(new File(filePath, savedName));  // 객체 셩성(파일패스, 파일명)
        redirectAttributes.addFlashAttribute("message", "외부 단일 파일 업로드 성공!");  // redirectAttributes 경로에
        // img : 서버로 이미지에 대한 요청을 보낼주소
        redirectAttributes.addFlashAttribute("img", "/uploads/img/single/" + savedName);  // redirectAttributes 경로에
        redirectAttributes.addFlashAttribute("singleFileDescription", singleFileDescription);  // redirectAttributes 경로에

        return "redirect:/result";
    }

    private String createSavedName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자
        // UUID 문자열로 변경후 "-" 지우고 확장자 붙이기
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
