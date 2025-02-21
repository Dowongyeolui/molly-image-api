package org.hidevelop.mollyimageapi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hidevelop.mollyimageapi.dto.CommonImageDto;
import org.hidevelop.mollyimageapi.dto.DeleteImageResDto;
import org.hidevelop.mollyimageapi.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image Controller", description = "이미지 관련 컨트롤러")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드", description = "이미지 업로드 시 URL 반환, isProduct : false 면 Review")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonImageDto> uploadImage(@RequestPart MultipartFile image, @RequestParam boolean isProduct) {
        String uniqueFilName = imageService.uploadImage(image, isProduct);

        return ResponseEntity.ok(new CommonImageDto(uniqueFilName));
    }

    @Operation(summary = "이미지 수정", description = "이미지 수정 URL 반환")
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateImage(
            @RequestPart MultipartFile image,
            @RequestPart CommonImageDto deleteUrl){

        char c = deleteUrl.url().charAt(8);
        boolean isProduct = c == 'p';


        imageService.deletedImage(deleteUrl.url(), isProduct);
        String result = imageService.uploadImage(image, isProduct);

        return ResponseEntity.ok(new CommonImageDto(result));
    }

    @Operation(summary = "이미지 삭제", description = "이미지 삭제")
    @DeleteMapping()
    public ResponseEntity<?> deleteImage(@RequestParam String url){
        char c = url.charAt(8);
        boolean isProduct = c == 'p';

        boolean isSuccess = imageService.deletedImage(url, isProduct);
        return ResponseEntity.ok(new DeleteImageResDto(isSuccess));
    }
}
