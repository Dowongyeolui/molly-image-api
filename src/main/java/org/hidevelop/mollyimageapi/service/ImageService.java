package org.hidevelop.mollyimageapi.service;

import org.hidevelop.mollyimageapi.exception.CustomError;
import org.hidevelop.mollyimageapi.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import static org.hidevelop.mollyimageapi.exception.CustomError.*;


@Service
public class ImageService {

    @Value("${image.product.dir}")
    private String productImageDir;

    @Value("${image.product.url}")
    private String productImageUrl;

    @Value("${image.review.url}")
    private String reviewImageUrl;

    @Value("${image.review.dir}")
    private String reviewImageDir;

    public String uploadImage(MultipartFile image, boolean isProduct) {

        // 불필요한 경로 제거
        String filename =
                StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()))
                        .replaceAll(" ", "")
                        .substring(0, 10);

        String extend = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        // 유니크한 파일 이름 설정 ex) 3f9b1c68-58f7-4d8c-8f5d-34f90fdb6a73_my_photo.jpg
        String uniqueFileName = UUID.randomUUID() + "_" + filename + extend;

        Path targetLocation;

        if(isProduct){
            targetLocation =
                    Paths.get(productImageDir).resolve(uniqueFileName);
        } else {
            targetLocation =
                    Paths.get(reviewImageDir).resolve(uniqueFileName);
        }

        try {
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw new CustomException(FAILED_UPLOAD);
        }

        if (isProduct) {
            uniqueFileName = productImageUrl + uniqueFileName;
        } else {
            uniqueFileName = reviewImageUrl + uniqueFileName;
        }

        return uniqueFileName;

    }

    public boolean deletedImage(String deletedImagePath, boolean isProduct){

        boolean isSuccess = false;

        deletedImagePath = deletedImagePath.substring(16);

        if(isProduct){
            deletedImagePath = productImageDir + deletedImagePath;
        } else {
            deletedImagePath = reviewImageDir + deletedImagePath;
        }

        File imageFile = new File(deletedImagePath);

        if (imageFile.exists()) {
            if (!imageFile.delete()) {
                throw new CustomException(FAILED_DELETE);
            }
            isSuccess = true;
        } else {
            throw new CustomException(FAILED_FIND);
        }
        return isSuccess;
    }
}
