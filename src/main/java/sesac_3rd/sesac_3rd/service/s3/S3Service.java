package sesac_3rd.sesac_3rd.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sesac_3rd.sesac_3rd.exception.CustomException;
import sesac_3rd.sesac_3rd.exception.ExceptionStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    // 외부에서 사용할 public 메서드
    public String upload(MultipartFile image) {
        // 입력받은 파일이 빈 파일인지 검증
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new CustomException(ExceptionStatus.EMPTY_FILE);   // 400
        }
        // uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환
        return this.uploadImg(image);
    }

    private String uploadImg(MultipartFile image) {
        try {
            // S3에 저장된 이미지의 public url을 받아 리턴
            return this.uploadImgToS3(image);
        } catch (IOException e) {
            throw new CustomException(ExceptionStatus.IO_EXCEPTION_ON_IMAGE_UPLOAD); // 500
        }
    }

    // 직접 S3로 업로드
    private String uploadImgToS3(MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();   // 원본 파일 명
        log.info("originImageName ----" + originalFileName);
        String extention = originalFileName.substring(originalFileName.lastIndexOf("."));   // 확장자명
        log.info("extention/.//././" + extention);

        String s3FileName = UUID.randomUUID().toString().substring(0,10)+originalFileName;   // s3에 올라갈 변경된 파일명
        log.info("s3FileNAme !@!!!!!!!" + s3FileName);

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);   // image를 byte[]로 변환

        // S3 객체는 파일 내용뿐 아니라, 객체의 유형, 크기, 캐싱 설정, 사용자 정의 메타데이터 등을 포함하는 여러 메타데이터 정보를 가질 수 있음
        ObjectMetadata metadata = new ObjectMetadata();  // metadata 생성
        metadata.setContentType("image/" + extention);   // 객체의 MIME 타입 설정
        metadata.setContentLength(bytes.length);    // 객체의 크기를 바이트 단위로 설정
//        metadata.setContentDisposition("inline");   // 브라우저에서 이미지 열수 있도록 설정

        // S3에 요청할 때 사용할 byteInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            // S3로 putObject 할 때 사용할 요청 객체
            // 생성자 : bucket 이름, 파일 명, byteInputStream, metadata
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucket, s3FileName, byteArrayInputStream, metadata);
//                            .withCannedAcl(CannedAccessControlList.PublicRead);  // 파일 접근 권한을 공개 읽기(Public Read)로 설정

            // 실제로 S3에 이미지 삽입
            amazonS3Client.putObject(putObjectRequest);
        } catch (Exception e){
            log.info("put error ??????@#$$^$^%$^%" + e.getMessage());
            throw new CustomException(ExceptionStatus.PUT_OBJECT_EXCEPTION);    // 500
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        // S3에 저장된 이미지의 public url을 받아 리턴
        return amazonS3Client.getUrl(bucket, s3FileName).toString();
    }

    // 이미지의 public url을 사용해 S3에서 해당 이미지 제거
    public void deleteImgFromS3(String imgAddress){
        String key = getKeyFromImgAddress(imgAddress);
        try {
            // 실제로 S3에서 이미지 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        } catch (Exception e){
            log.info("delete error <><><><><>>><>< " + e.getMessage());
            throw new CustomException(ExceptionStatus.IO_EXCEPTION_ON_IMAGE_DELETE);   // 500
        }
    }

    // 이미지 주소(imageAddress)에서 S3 객체의 키(key)를 추출
    private String getKeyFromImgAddress(String imgAddress){
        try {
            URL url = new URL(imgAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1);  // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e){
            // MalformedURLException: URL이 유효하지 않을 때 발생. URL 형식 오류.
            // UnsupportedEncodingException: 지원되지 않는 문자 인코딩을 사용할 때 발생.
            throw new CustomException(ExceptionStatus.IO_EXCEPTION_ON_IMAGE_DELETE);   // 500
        }
    }
}
