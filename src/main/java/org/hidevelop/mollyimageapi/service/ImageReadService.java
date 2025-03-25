package org.hidevelop.mollyimageapi.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.hidevelop.mollyimageapi.exception.CustomError;
import org.hidevelop.mollyimageapi.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
public class ImageReadService {

    public byte[] convertToWebp(File originalFile, int w, int h, boolean webpSupported) {

        try {
            ImmutableImage image = ImmutableImage.loader().fromFile(originalFile);
            ImmutableImage resized = image.scaleTo(w, h);

//            return Files.readAllBytes(originalFile.toPath());
            if (webpSupported) {
                //Q : (0~100) 높을수록 이미지 품질 업, 파일 크기 업
                //M : (0~6) 값이 높을수록 압축 속도는 느려짐, 압축률 높아짐
                //Z : (0~9) 값이 높을수록 높은 압축률, 파일 크기 제한, 압축 속도는 느려짐, 주로 무손실
                WebpWriter writer = WebpWriter.DEFAULT.withQ(70).withM(3);
                return resized.bytes(writer);
            } else {
                // 원본 포맷 유지
                return Files.readAllBytes(originalFile.toPath());
            }
        } catch (IOException e){
            throw new CustomException(CustomError.FAILED_CONVERT_WEBP);
        }
    }


}
