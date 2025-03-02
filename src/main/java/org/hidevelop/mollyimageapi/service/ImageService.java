package org.hidevelop.mollyimageapi.service;

import org.hidevelop.mollyimageapi.exception.CustomException;
import org.hidevelop.mollyimageapi.type.AllowedExtension;
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


    /***
     * 이미지 업로드
     * @param image 업로드 할 이미지
     * @param isProduct 상품이미지인가? 리뷰이미지인가?
     * @return 조회할 수 있는 URL
     */
    public String uploadImage(MultipartFile image, boolean isProduct) {

        // 불필요한 경로 제거
        String imageNameWithExtend =
                StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()))
                        .replaceAll(" ", "");

        //ImageName과 extension 분리 기준
        int lastDotIndex = imageNameWithExtend.lastIndexOf(".");
        String imageName = imageNameWithExtend.substring(0, lastDotIndex);
        String extension = imageNameWithExtend.substring(lastDotIndex);

        //이미지 유효성 검사
        validImage(imageName, extension);

        //이미지 이름 단축
        imageName = shorteningImageName(imageName);

        //이미지 이름 중복 방지
        String uniqueFileName = UUID.randomUUID() + "_" + imageName + extension;
        
        // 어느 폴더에 저장할까요~~~
        Path targetLocation = getPathToSave(isProduct, uniqueFileName);


        // 저장할 껀데..혹시나 혹시나 아주 혹시나 똑같은 이름이 있으면 어떻게하죠? 그럴 일 없어요 ㅠ
        try {
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw new CustomException(FAILED_UPLOAD);
        }

        // 어느 경로로 조회할까요? yogiyo
        uniqueFileName = getPathToView(isProduct, uniqueFileName);
        return uniqueFileName;

    }

    /***
     * 조회할 url 적용 함수
     * @param isProduct 상품인가요?
     * @param uniqueFileName 유니크하게 만들어줬어요 ㅋ
     * @return 유니크한 파일네임
     */
    private String getPathToView(boolean isProduct, String uniqueFileName) {
        if (isProduct) {
            uniqueFileName = productImageUrl + uniqueFileName;
        } else {
            uniqueFileName = reviewImageUrl + uniqueFileName;
        }
        return uniqueFileName;
    }

    private Path getPathToSave(boolean isProduct, String uniqueFileName) {
        Path targetLocation;
        if(isProduct){
            targetLocation =
                    Paths.get(productImageDir).resolve(uniqueFileName);
        } else {
            targetLocation =
                    Paths.get(reviewImageDir).resolve(uniqueFileName);
        }
        return targetLocation;
    }

    /***
     * 이미지 이름 단축~!
     * @param imageName 단축할 이미지 이름
     * @return 단축된 이미지 이름
     */
    private static String shorteningImageName(String imageName) {
        if (imageName.length() > 10){
            imageName = imageName.substring(0, 10);
        }
        return imageName;
    }

    /***
     * 이미지 유효성 검증
     * @param imageName 이미지 이름
     * @param extension 이미지 확장자
     */
    private static void validImage(String imageName, String extension) {
        
        //이미지 이름이 비어있으면 안돼!
        if (imageName.isEmpty()){
            throw new CustomException(SHORT_FILE_NAME);
        }

        //이미지 확장자는 서버가 정한 녀석들만 받아들일 수 있어요!
        if (!isValidExtension(extension)){
            throw new CustomException(DISALLOWED_EXTEND);
        }
    }

    /***
     * 이미지 삭제 함수
     * @param deletedImagePath 삭제할 이미지 경로 보내주세요!
     * @param isProduct 상품이에요? 리뷰에요?
     * @return 삭제 성공! 실패 ㅠ
     */
    public boolean deletedImage(String deletedImagePath, boolean isProduct){

        boolean isSuccess = false;

        if(isProduct){
            deletedImagePath = deletedImagePath.substring(16);
            deletedImagePath = productImageDir + deletedImagePath;
        } else {
            deletedImagePath = deletedImagePath.substring(15);
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

    /***
     * 다른 파일은 감히?? 저장한다고?? 내 라즈베리에?? 진짜?? 해킹파일? bash? exe? sh? 어림 없지 ㅋ
     * 봐줘..해킹하지마 제발..봐주세요 ㅠㅠ 취약점 알려주실꺼면, dlwpdbs1229@naver.com로 메일 주세요
     * Donation 해드릴게요 ㅠ
     * 중요한 것 없어요. 고양이 사진만 잔뜩이에요 ㅠ 강해져서 돌아옴 ㅅㄱ
     * @param extend 확장자
     * @return 유효한가 여부
     */
    private static boolean isValidExtension(String extend) {
        for (AllowedExtension ext : AllowedExtension.values()) {
            if (extend.toLowerCase().equals(ext.getText())) {
                return true;
            }
        }
        return false;
    }
}
