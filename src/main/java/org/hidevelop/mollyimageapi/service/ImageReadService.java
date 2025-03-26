package org.hidevelop.mollyimageapi.service;

import com.sksamuel.scrimage.ImmutableImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.hidevelop.mollyimageapi.exception.CustomError;
import org.hidevelop.mollyimageapi.exception.CustomException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageReadService {

    public byte[] convertToWebp(File originalFile, int w, int h, boolean webpSupported) {
        try {
            // 이미지 리사이징 (임시 PNG 파일로 저장)
            ImmutableImage image = ImmutableImage.loader().fromFile(originalFile);
            ImmutableImage resized = image.scaleTo(w, h);

            BufferedImage bufferedImage = resized.toNewBufferedImage(BufferedImage.TYPE_INT_ARGB);

            File tempPng = File.createTempFile("resized-", ".png");
            ImageIO.write(bufferedImage, "png", tempPng);

            // WebP 지원 시 cwebp CLI 실행
            if (webpSupported) {
                File outputWebp = File.createTempFile("converted-", ".webp");

                ProcessBuilder pb = new ProcessBuilder(
                    "cwebp", "-q", "70", // 품질 설정
                    tempPng.getAbsolutePath(),
                    "-o", outputWebp.getAbsolutePath()
                );
                Process process = pb.start();

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    log.error("cwebp 실행 실패. 종료 코드: {}", exitCode);
                    throw new CustomException(CustomError.FAILED_CONVERT_WEBP);
                }

                return Files.readAllBytes(outputWebp.toPath());
            } else {
                // WebP 미지원 시 원본 반환
                return Files.readAllBytes(originalFile.toPath());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error Message: {}", e.getMessage(), e);
            throw new CustomException(CustomError.FAILED_CONVERT_WEBP);
        }
    }



}
