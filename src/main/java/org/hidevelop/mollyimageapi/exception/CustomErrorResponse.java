package org.hidevelop.mollyimageapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomErrorResponse {

    private final String message;

}
