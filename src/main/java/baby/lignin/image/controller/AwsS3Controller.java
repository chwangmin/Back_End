package baby.lignin.image.controller;

import baby.lignin.image.service.AwsS3Service;
import baby.lignin.support.ApiResponse;
import baby.lignin.support.ApiResponseGenerator;
import baby.lignin.support.MessageCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class AwsS3Controller {
    private final AwsS3Service awsS3Service;

    @Operation(summary = "S3에 이미지 업로드", description = "S3에 이미지를 업로드하여 링크를 생성합니다.")
    @PostMapping("/upload")
    public ApiResponse<ApiResponse.SuccessBody<String>> uploadImage(@RequestPart(value = "file") MultipartFile multipartFile) {
        return ApiResponseGenerator.success(awsS3Service.uploadImage(multipartFile), HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
    }
}
