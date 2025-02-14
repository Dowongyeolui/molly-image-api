package org.hidevelop.mollyapiapi.service;

import org.hidevelop.mollyapiapi.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static org.hidevelop.mollyapiapi.exception.CustomError.*;


@Service
public class ImageService {

    @Value("${image.product.dir}")
    private String productImageDir;


    @Value("${image.review.dir}")
    private String reviewImageDir;

    public String uploadImage(MultipartFile image, boolean isProduct) {

        // 불필요한 경로 제거
        String filename =
                StringUtils.cleanPath(image.getOriginalFilename())
                        .replaceAll(" ", "")
                        .substring(0, 10);

        // 유니크한 파일 이름 설정 ex) 3f9b1c68-58f7-4d8c-8f5d-34f90fdb6a73_my_photo.jpg
        String uniqueFileName = UUID.randomUUID() + "_" + filename;

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

        return uniqueFileName;
    }
}
