# Spring MVC 과제

세 챕터의 요청 매핑, 핸들러 메서드, 뷰 처리를 연습하는 Gradle 프로젝트입니다.

## 실행

1. `build.gradle`이 있는 폴더를 IntelliJ IDEA에서 엽니다.
2. Project SDK와 Gradle JVM을 **JDK 21**로 설정합니다.
3. 터미널에서 실행합니다.

```powershell
.\gradlew.bat bootRun
```

[http://localhost:8081/](http://localhost:8081/)을 엽니다. 같은 포트를 사용하는 수업 서버가 실행 중이면 먼저 종료합니다.

Gradle Wrapper를 포함하므로 별도 Gradle 설치는 필요 없습니다. 처음 실행할 때는 인터넷 연결이 필요합니다. macOS/Linux에서는 `chmod +x gradlew` 후 `./gradlew bootRun`을 사용합니다.

## 구현할 Controller 5개

**각 파일/메서드 위 주석만 읽고 구현합니다.** 주석에 요청 경로, 입력값, 사용할 방식, 화면에 전달할 값과 결과가 적혀 있습니다. `problem01`부터 순서대로 진행합니다.

파일 위치는 `src/main/java/com/bear/mvcassignment` 아래입니다.

| 파일 | 내용 |
|---|---|
| `problem01/MappingController.java` | 공통 경로, GET/POST, 여러 URL, 경로 변수 |
| `problem02/ParameterController.java` | WebRequest, RequestParam, 기본값, void 뷰 |
| `problem03/ReservationController.java` | DTO 바인딩, 요금 계산, 예약 완료 이동 |
| `problem04/SessionBodyController.java` | 세션, 요청 본문, 헤더, 쿠키 |
| `problem05/ViewController.java` | String, ModelAndView, 리다이렉트, flash |

필요한 import와 어노테이션을 추가하고 TODO를 완성합니다. 메서드 이름·반환형은 유지합니다. 실행한 화면의 링크와 폼으로 동작을 확인합니다.

`ReservationForm.java`는 예약 입력값을 묶는 완성된 DTO입니다. 수정하지 않습니다.

`MvcAssignmentApplication.java`, `support` 패키지, HTML, JavaScript, 설정과 빌드 파일은 제공 코드이므로 수정하지 않습니다. 처음에는 홈만 열리며, 구현하지 않은 경로의 404 응답은 정상입니다.
