package org.example.fileupload.controller;

import org.example.fileupload.dto.FileDTO;
import org.example.fileupload.util.FileUploadUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* [External] 외부폴더 저장 방식
* - 업로드 파일을 application.yaml에 설정한 외부 경로(file.upload.windows/mac.path)에 저장
* - 요청 주소 : /external-single-file
* - 이미지 주소 : /uploads/img/single/파일명 (WebConfig의 addResourceHandlers로 실제 폴더와 연결)
* - 해결 : 빌드폴더와 상관없이 저장되므로 재시작 없이 바로 이미지를 볼 수 있다
* */
@Controller
public class ExternalFileUploadController {

    private final FileUploadUtils fileUploadUtils;

    public ExternalFileUploadController(FileUploadUtils fileUploadUtils) {
        this.fileUploadUtils = fileUploadUtils;
    }

    // 요청주소
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

    /*
    * 외부 경로 다중 파일 업로드
    * - List<MultipartFile> : 같은 name(multiFiles)으로 넘어온 파일 여러 개를 한 번에 받음
    * - 파일마다 UUID 저장 파일명을 만들어 외부 경로(img/multi)에 저장
    * - FileDTO 리스트 : 저장한 파일 정보를 기록 (실패 시 삭제할 대상 목록으로 사용)
    * - savedFilesPaths : 결과 화면에서 이미지를 보여줄 주소 목록 (/uploads/img/multi/파일명)
    * - 중간에 실패하면 catch에서 이미 저장한 파일을 모두 삭제 (일부만 저장되는 것 방지)
    *
    * 여러개의 이미지를 받는건 동일한데
    * try Catch -> database에 저장되는것, 실제 파일이 저장되는것을 둘다 성공했을때만 완료처리가 되어야 한다.
    *
    * 둘다성공했을때?
    * 둘 중 하나만 성공하면 데이터가 서로 맞지 않게 됩니다.
    * - 파일만 저장되고 DB 저장이 실패한 경우: 폴더에 아무도 쓰지 않는 쓰레기 파일이 남습니다.
    * - DB만 저장되고 파일 저장이 실패한 경우: 화면에서 이미지를 불러오면 엑박(깨진 이미지)이 뜹니다.
    *   그래서 "전부 성공하거나, 하나라도 실패하면 전부 취소"하도록 try-catch로 감싸는 것입니다.
    * */
    @PostMapping("external-multi-file")
    public String externalMultiFileUpload(@RequestParam List<MultipartFile> multiFiles,
                                          @RequestParam String multiFileDescription,
                                          RedirectAttributes redirectAttributes) {

        // 여러 파일들도 하나씩 저장하므로 Multipart File 과정을 반복
        String uploadPath = fileUploadUtils.getUploadPath("img/multi");
        List<FileDTO> files = new ArrayList<>();
        List<String> savedFilesPaths = new ArrayList<>();

        // 리스트 꺼내기
        try {
            for (MultipartFile multiFile : multiFiles) {
                // 원본 이름꺼내기
                String originalFileName = multiFile.getOriginalFilename();

                // UUID 만들기
                String savedName = createSavedName(originalFileName);

                // DTO LIST에 추가
                files.add(new FileDTO(originalFileName, savedName, uploadPath, multiFileDescription));

                // 파일 저장
                multiFile.transferTo(new File(uploadPath, savedName));

                // 경로 LIST에 담기 -> FlashAttribute로 리다이렉트 대상 화면에 어트리뷰트를 넘겨주기 위해
                savedFilesPaths.add("/uploads/img/multi/" + savedName);
            }

            redirectAttributes.addFlashAttribute("message", "[Success] 외부 경로에 다중 파일 업로드 성공!");
            redirectAttributes.addFlashAttribute("imgs", savedFilesPaths);
            redirectAttributes.addFlashAttribute("multiFileDescription", multiFileDescription);
        } catch (Exception exception) {
            for (FileDTO file : files) {

                // 예외가 발생하면 list에서 파일삭제
                new File(uploadPath, file.getSavedName()).delete();
            }
            redirectAttributes.addFlashAttribute("message", "[Failed] 외부 경로에 다중 파일 업로드 실패!!");
        }

        return "redirect:/result";
    }


    private String createSavedName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자
        // UUID 문자열로 변경후 "-" 지우고 확장자 붙이기
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
