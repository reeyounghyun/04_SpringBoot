# chap06-file-upload

## 외부폴더 저장 방식 (External)

### 1. 만드는 순서 (설정 → 코드)

| 순서 | 파일 | 하는 일 |
|---|---|---|
| ① | `application.yaml` | 파일 크기 제한(10MB) + 저장 경로(`file.upload.windows.path` / `mac.path`) 설정 |
| ② | `util/FileUploadUtils.java` | `@Value`로 yaml 경로를 읽고 → OS에 맞는 경로 선택 → 폴더 없으면 생성 → 경로 반환 |
| ③ | `config/WebConfig.java` | `/uploads/**` 주소를 실제 폴더(`file:///경로/`)에 연결 (이게 있어야 브라우저에서 이미지가 보임) |
| ④ | `controller/ExternalFileUploadController.java` | 업로드 요청 받아서 파일 저장 → 결과 페이지로 이동 |
| ⑤ | `templates/main.html` | 업로드 폼 (`action="/external-single-file"`, `enctype="multipart/form-data"`) |
| ⑥ | `templates/result.html` | 업로드 결과 메시지와 이미지 출력 |

### 2. 업로드할 때 실행되는 순서 (단일 파일)

```
[main.html] 파일 선택 → 업로드 버튼
   ↓ POST /external-single-file
[Controller] externalSingleFileUpload()
   ① fileUploadUtils.getUploadPath("img/single")   → 저장할 외부 경로 구하기
   ② singleFile.getOriginalFilename()               → 원본 파일명
   ③ createSavedName(originalFileName)              → UUID 저장 파일명 생성
   ④ transferTo(new File(filePath, savedName))      → 실제 폴더에 저장
   ⑤ addFlashAttribute("img", "/uploads/img/single/" + savedName) → 보여줄 주소 담기
   ⑥ return "redirect:/result"
   ↓
[result.html] <img src="/uploads/img/single/UUID.jpg">
   ↓ 브라우저가 이미지 요청
[WebConfig] /uploads/** → file:///C:/.../uploadedFiles/ 로 연결
   ↓
실제 폴더의 이미지가 화면에 표시됨
```

### 3. 다중 파일은?

흐름은 같고, `for`문으로 ③~④를 파일 개수만큼 반복합니다.

- `FileDTO` 리스트에 저장한 파일 정보 기록
- 중간에 실패하면 `catch`에서 이미 저장한 파일 삭제
- 결과 화면에는 `imgs`(주소 목록)를 넘겨서 `result.html`의 `th:each`로 하나씩 출력
