package org.hidevelop.mollyapiapi.controller;


import lombok.RequiredArgsConstructor;
import org.hidevelop.mollyapiapi.dto.UploadImageResDto;
import org.hidevelop.mollyapiapi.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadImageResDto> uploadImage(@RequestPart MultipartFile image, @RequestParam boolean isProduct) {
        String uniqueFilName = imageService.uploadImage(image, isProduct);
        if (isProduct) {
            uniqueFilName = "/images/product/" + uniqueFilName;
        } else {
            uniqueFilName = "/images/review/" + uniqueFilName;
        }
        return ResponseEntity.ok(new UploadImageResDto(uniqueFilName));
    }
}
