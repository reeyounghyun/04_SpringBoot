package org.example.restapi.section05.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/*
* Swagger 문서용 인터페이스
* - Swagger 설명 어노테이션(@Tag, @Operation, @ApiResponses, @Parameter)만 모아두는 곳
* - 실제 동작 코드는 SwaggerTestControllerImpl 클래스가 구현(implements)
* - 문서 설명과 비즈니스 로직을 분리해서 컨트롤러 코드를 깔끔하게 유지
* */
@Tag(name="SWAGGER 테스트 컨트롤러", description = "Section #5의 스웨거 테스트입니다.")
public interface SwaggerTestController {

    // 유저 목록 전체 조회
    @Operation(summary = "모든 사용자 조회", description = "모든 사용자를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "500", description = "조회 실패", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<ResponseMessage> findAllUsers();

    // 특정 유저 상세 조회
    @Operation(summary = "특정 유저 상세 조회", description = "사용자 번호를 통해 특정 사용자 정보를 조회합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "500", description = "조회 실패", content = @Content(schema = @Schema(hidden = true)))
    })
    @Parameter(name = "userNo", description = "조회할 사용자 번호", required = true)
    public ResponseEntity<ResponseMessage> findUserByNo(int userNo);

    /* ==================== [연습 문제] ====================
     * SwaggerTestControllerImpl에 있는 나머지 API 3개도 Swagger 문서로 만들어 보세요.
     * 위의 findAllUsers(), findUserByNo() 작성 방식을 참고하면 됩니다.
     * (메서드 이름, 매개변수, 반환 타입은 Impl 클래스와 똑같아야 합니다!)
     *
     * [문제 1] 신규 유저 등록 - registUser
     *  - @Operation  : summary = "신규 유저 등록", description은 자유롭게 작성
     *  - @ApiResponses
     *      201 : "등록 성공"  (응답 본문이 없으므로 schema hidden 처리)
     *      400 : "잘못된 요청"
     *  - 반환 타입 : ResponseEntity<Void>
     *  - 매개변수 : UserDTO newUser
     *
     * [문제 2] 유저 정보 수정 - PutEntity
     *  - @Operation  : summary = "유저 정보 수정"
     *  - @ApiResponses
     *      200 : "수정 성공"
     *      404 : "수정할 회원 없음"
     *  - @Parameter : userNo → "수정할 사용자 번호", 필수값
     *  - 반환 타입 : ResponseEntity<Void>
     *  - 매개변수 : int userNo, UserDTO newUser
     *
     * [문제 3] 유저 삭제 - removeUser
     *  - @Operation  : summary = "유저 삭제"
     *  - @ApiResponses
     *      204 : "삭제 성공" (No Content : 응답 본문 없음)
     *      404 : "삭제할 회원 없음"
     *  - @Parameter : userNo → "삭제할 사용자 번호", 필수값
     *  - 반환 타입 : ResponseEntity<?>
     *  - 매개변수 : int userNo
     *
     * [확인 방법]
     *  1. 컴파일 오류가 없는지 확인 (Impl의 메서드와 선언이 일치해야 함)
     *  2. 서버 재시작 → http://localhost:8080/swagger-ui.html
     *  3. "SWAGGER 테스트 컨트롤러"에 POST / PUT / DELETE 설명이 나오면 성공!
     *
     * [보너스] Impl 클래스의 각 메서드 위에 @Override를 붙여 보세요.
     *         인터페이스와 선언이 다르면 컴파일 오류로 바로 알려줍니다.
     * ==================================================== */

}
