package org.hidevelop.mollyimageapi.controller;

import static org.hidevelop.mollyimageapi.exception.CustomError.FAILED_FIND;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import org.hidevelop.mollyimageapi.exception.CustomException;
import org.hidevelop.mollyimageapi.service.ImageReadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image Read Controller", description = "이미지 조회 관련 컨트롤러")
@RequestMapping("/images")
@RestController
@RequiredArgsConstructor
public class ImageReadController {

    @Value("${image.product.dir}")
    private String productImageDir;

    @Value("${image.review.dir}")
    private String reviewImageDir;

    private final ImageReadService imageReadService;

    @Operation(summary = "이미지 조회", description = "이미지 조회/ .webp/ w = with,h = height")
    @GetMapping("/{type}/{filename}")
    public ResponseEntity<byte[]> getImage(
        @PathVariable String type,
        @PathVariable String filename,
        HttpServletRequest request,
        @RequestParam(defaultValue = "200") int w,
        @RequestParam(defaultValue = "300") int h) {


        String folderPath = type.equals("product") ? productImageDir : reviewImageDir;
        File imageFile = new File(folderPath + filename);
        if (!imageFile.exists()) {
            throw new CustomException(FAILED_FIND);
        }

        boolean webpSupported = isWebSupported(request);
        byte[] imageBytes = imageReadService.convertToWebp(imageFile, w, h, webpSupported);

        String contentType = webpSupported ? "image/webp" : getMimeType(imageFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    private static String getMimeType(File imageFile) {
        try {
            return Files.probeContentType(imageFile.toPath());
        } catch (IOException e){
            throw new CustomException(FAILED_FIND);
        }

    }

    public boolean isWebSupported(HttpServletRequest request){
        String accept = request.getHeader("Accept");
        return accept != null &&( accept.contains("image/webp") || accept.contains("*/*"));
    }

}
