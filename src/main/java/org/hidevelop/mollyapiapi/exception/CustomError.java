package org.hidevelop.mollyapiapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomError {

    FAILED_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다")
    ;

    private final HttpStatus status;
    private final String message;
}
