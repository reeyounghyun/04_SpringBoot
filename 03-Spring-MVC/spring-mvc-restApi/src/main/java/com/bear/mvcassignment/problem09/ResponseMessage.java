package com.bear.mvcassignment.problem09;

import java.util.Map;

/**
 * 제공 코드: API 응답 바디를 감싸는 완성된 클래스. 수정하지 않는다.
 *
 * 예) {"httpStatus":200,"message":"조회성공","result":{"book":{...}}}
 *     {"httpStatus":404,"message":"도서를 찾을 수 없습니다.","result":null}
 */
public class ResponseMessage {

    private int httpStatus;
    private String message;
    private Map<String, Object> result;

    public ResponseMessage() {
    }

    public ResponseMessage(int httpStatus, String message, Map<String, Object> result) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.result = result;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getResult() {
        return result;
    }
}
