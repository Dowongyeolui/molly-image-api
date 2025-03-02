package org.hidevelop.mollyimageapi.type;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AllowedExtension {

    JPG(".jpg"),
    PNG(".png"),
    GIF(".gif"),
    BMP(".bmp"),
    JPEG(".jpeg"),
    WEBP(".webp")
    ;

    private final String text;
}
