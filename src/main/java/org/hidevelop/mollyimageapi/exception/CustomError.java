package org.hidevelop.mollyimageapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomError {

    FAILED_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다"),
    FAILED_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."),
    FAILED_FIND(HttpStatus.BAD_REQUEST, "삭제할 이미지가 존재하지 않습니다."),
    FAILED_TRANSFER_LOG(HttpStatus.BAD_REQUEST, "로그 전송에 실패했습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
