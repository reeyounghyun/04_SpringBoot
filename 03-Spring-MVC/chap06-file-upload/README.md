# chap06-file-upload

Spring MVC 파일 업로드 실습 프로젝트입니다.
같은 "파일 업로드"를 **저장 위치만 바꿔서** 3가지 방식으로 비교합니다.

## 업로드 방식 3가지 비교

| 구분 | Basic (기본) | Internal (빌드폴더 내부) | External (외부폴더) |
|---|---|---|---|
| 컨트롤러 | `BasicFileUploadController` | `InternalFileUploadController` | `ExternalFileUploadController` |
| 요청 주소 | `/basic-file-upload` | `/single-file` | `/external-single-file`, `/external-multi-file` |
| 저장 위치 | 프로젝트 `uploads/` 폴더 | `static/uploadedFiles/img/single` | yaml에 설정한 외부 경로 `uploadedFiles/img/...` |
| 이미지 주소 | 없음 (화면에서 못 봄) | `/uploadedFiles/img/single/파일명` | `/uploads/img/single(multi)/파일명` |
| 저장 파일명 | 원본 이름 그대로 | UUID | UUID |
| 문제점 / 해결 | 경로 하드코딩, 같은 이름이면 덮어쓰기, 브라우저에서 못 봄 | 서버는 빌드폴더를 읽어서 재시작해야 새 이미지가 보일 수 있음 | 빌드폴더와 상관없이 저장되므로 재시작 없이 바로 보임 |

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
- 중간에 실패하면 `catch`에서 이미 저장한 파일 삭제 (일부만 저장되는 것 방지)
- 결과 화면에는 `imgs`(주소 목록)를 넘겨서 `result.html`의 `th:each`로 하나씩 출력
- 폼에서는 `<input type="file" name="multiFiles" multiple>` → `multiple`이 있어야 여러 개 선택 가능

> 실무에서는 **DB 저장 + 실제 파일 저장이 둘 다 성공했을 때만** 완료 처리해야 합니다.
> DB는 `@Transactional`로 자동 롤백되지만, 폴더에 저장된 파일은 자동으로 취소되지 않아 catch에서 직접 삭제합니다.

## 핵심 개념 정리

| 개념 | 설명 |
|---|---|
| `enctype="multipart/form-data"` | 파일을 전송하려면 form에 꼭 필요 |
| `MultipartFile` | 업로드된 파일 1개를 받는 객체 (`List<MultipartFile>`이면 여러 개) |
| `@RequestParam` 이름 | input의 `name`과 파라미터 이름이 같아야 받을 수 있음 |
| `transferTo(File)` | 업로드된 파일을 지정한 위치에 저장 |
| UUID | `UUID.randomUUID().toString().replace("-", "") + 확장자` → 이름 중복(덮어쓰기) 방지 |
| `@Value("${...}")` | yaml에 만든 사용자 정의 값을 필드에 주입 |
| `addAttribute` vs `addFlashAttribute` | addAttribute는 URL 쿼리스트링으로 전달, addFlashAttribute는 리다이렉트 후 화면의 model로 1회 전달 |
| `WebMvcConfigurer.addResourceHandlers` | URL 패턴(`/uploads/**`)을 실제 폴더 경로에 연결 |
| `ResourceLoader` | `classpath:` 같은 Spring 관리 리소스 위치를 조회 (Internal 방식에서 사용) |

## 실행 / 테스트 방법

1. `application.yaml`의 `file.upload.windows.path`(또는 `mac.path`)를 내 PC 경로로 확인
2. 서버 실행 후 `http://localhost:8080` 접속
3. 각 폼에서 파일 선택 → 업로드
4. 결과 화면(`/result`)에서 메시지와 이미지 확인
5. 저장 폴더에 UUID 이름으로 파일이 생겼는지 확인
   - Basic → `uploads/`
   - Internal → `src/main/resources/static/uploadedFiles/img/single/`
   - External → `uploadedFiles/img/single/`, `uploadedFiles/img/multi/`

## 겪었던 에러와 해결

| 에러 | 원인 | 해결 |
|---|---|---|
| 413 Payload Too Large | 기본 업로드 제한이 1MB | yaml에 `max-file-size`, `max-request-size` 설정 (다중 업로드는 **합계** 기준) |
| 404 (`/result`) | `/result` GetMapping이 없음 / 서버 재시작 전 | `@GetMapping("/result")` 추가 후 재시작 |
| 404 (이미지) | 이미지 주소에 `/static/`을 붙임 | static 폴더는 루트(`/`)로 서비스되므로 `/static/` 제거 |
| Ambiguous mapping | 같은 URL을 두 컨트롤러에서 매핑 | 한 곳에만 남기기 |
| 결과 화면이 비어 있음 | 리다이렉트인데 `addAttribute` 사용 | `addFlashAttribute`로 변경 |
| `th:else` 동작 안 함 | Thymeleaf에는 `th:else`가 없음 | `th:unless` 사용 |
| 400 Bad Request | input `name`과 `@RequestParam` 이름 불일치 | 이름 맞추기 (`multiFiles`, `multiFileDescription`) |
