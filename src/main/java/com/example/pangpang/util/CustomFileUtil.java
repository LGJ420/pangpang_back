package com.example.pangpang.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

//CustomFileUtil은 파일 데이터의 입출력을 담당한다 Product에 이미지 올릴때 사용
@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    // 설정 파일(application.properties)에서 com.example.upload.path 값을 주입받아 
    // uploadPath에 저장
    @Value("${com.example.upload.path}")
    private String uploadPath;

    // 객체가 생성된 후 초기화 메서드 실행
    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);

        // tempFolder가 가리키는 파일이나 디렉토리가 실제로 존재하는지 여부를 확인
        if(tempFolder.exists() == false){
            // 파일, 디렉토리가 존재하지 않으면 File 객체가 가리키는 디렉토리를 생성
            tempFolder.mkdir();
        }

        // tempFolder는 File 객체로, uploadPath에 할당될 디렉토리의 경로를 나타냄
        uploadPath = tempFolder.getAbsolutePath();

        log.info("------------------------------");
        log.info(uploadPath);
    }


    // 새로 추가한 메서드 (작동 실패시 점검필요)
    // 파일 하나 저장 메서드
    // 파일 하나를 서버에 저장하며, 이미지 파일일 경우 썸네일 생성
    public String saveFile(MultipartFile file) throws RuntimeException {
        
        if (file == null) { 
            return "";
        }

        String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 파일이 저장될 서버의 경로를 설정
        Path savePath = Paths.get(uploadPath, savedName);

        try {
            // 파일의 입력 스트림을 읽어 서버의 지정된 경로에 파일을 저장
            // 클라이언트에서 업로드한 파일을 서버에 저장
            Files.copy(file.getInputStream(), savePath);

            // 파일의 MIME 타입을 확인
            String contentType = file.getContentType();

            // 이미지 파일인 경우 썸네일 생성
            if(contentType != null && contentType.startsWith("image")){

                // 썸네일은 s_ 접두사를 붙여서 원본 파일과 구분
                Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);

                Thumbnails.of(savePath.toFile())
                    .size(200, 200)
                    .toFile(thumbnailPath.toFile());
            }
        }
        catch (IOException e) {
            // 파일 저장 중 오류 발생 시 RuntimeException 발생
            throw new RuntimeException(e.getMessage());
        }
        
        // 저장된 파일의 이름을 반환
        return savedName;
    }




    // 여러 파일 저장 메서드
    // 여러 파일을 서버에 저장하며, 이미지 파일일 경우 썸네일 생성
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        
        if (files == null || files.size() == 0) {   // 파일 리스트가 비어있거나 null일 경우 빈 리스트 반환
            return List.of();
        }

        // 저장된 파일의 이름을 담을 리스트 초기화
        List<String> uploadNames = new ArrayList<>();

        // 파일 처리 루프 - 파일 리스트의 각 파일을 순회하며 처리
        for(MultipartFile multipartFile : files) {
            if (multipartFile.isEmpty()) {
                continue; // 빈 파일은 건너뛰기
            }

            // 각 파일에 대해 고유한 이름 생성
            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

            // 파일이 저장될 서버의 경로를 설정
            Path savePath = Paths.get(uploadPath, savedName);

            try {
                // 클라이언트에서 업로드한 파일을 서버에 저장
                Files.copy(multipartFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

                // 파일의 MIME 타입을 확인
                String contentType = multipartFile.getContentType();

                // 이미지 파일인 경우 썸네일 생성
                if(contentType != null && contentType.startsWith("image")){

                    // 썸네일은 s_ 접두사를 붙여서 원본 파일과 구분
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);

                    Thumbnails.of(savePath.toFile())
                        .size(200, 200)
                        .toFile(thumbnailPath.toFile());
                }

                // 저장된 파일의 이름을 리스트에 추가
                uploadNames.add(savedName);
            }
            catch (IOException e) {     // 파일 저장 중 오류 발생 시 RuntimeException 발생
                throw new RuntimeException(e.getMessage());
            }
        }
        // 저장된 파일의 이름을 담고 있는 리스트 반환
        return uploadNames;
    }




    // 파일 조회 메서드
    // 반환값 : HTTP 응답 객체. 파일 데이터와 함께 적절한 HTTP 헤더를 설정하여 클라이언트에게 반환
    public ResponseEntity<Resource> getFile(String fileName){

        // 요청된 파일의 경로를 조합하여 FileSystemResource 객체를 생성
        // 서버의 파일 시스템에서 해당 파일을 참조할 수 있는 Resource 객체를 만듦
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        if(!resource.isReadable()){ // 생성한 Resource 객체가 읽기 가능한지 확인

            // 파일이 존재하지 않거나 읽을 수 없는 경우, 기본 이미지 파일(default.jpg)을 대신 사용
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }

        // HTTP 응답 헤더를 설정하기 위한 HttpHeaders 객체를 생성
        // 파일의 콘텐츠 타입을 클라이언트에게 명시하기 위해 헤더를 설정
        HttpHeaders headers = new HttpHeaders();

        try{
            // 파일의 MIME 타입을 결정하여 Content-Type 헤더에 추가
            // 클라이언트가 파일의 형식을 정확히 인식할 수 있도록 하기 위함
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }
        catch(Exception e){
            // MIME 타입을 결정하는 과정에서 오류가 발생하면, 내부 서버 오류 응답을 반환
            return ResponseEntity.internalServerError().build();
        }

        // 설정한 헤더와 함께 파일 리소스를 포함한 ResponseEntity 객체를 반환
        // 클라이언트에게 파일을 전달하고, 파일의 형식을 명시하는 HTTP 응답을 생성
        return ResponseEntity.ok().headers(headers).body(resource);
    }


    // 파일 삭제 메서드
    public void deleteFiles(List<String> fileNames){

        // fileNames 리스트가 null이거나 비어있는 경우, 메서드 종료
        if(fileNames == null || fileNames.size() == 0){
            return;
        }

        // forEach 메서드를 사용하여 파일 이름 목록을 반복
        fileNames.forEach(fileName->{

            //썸네일이 있는지 확인하고 삭제
            String thumbnailFileName = "s_" + fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);
            
            try{
                // Files.deleteIfExists 메서드를 사용하여 파일이 존재하면 삭제
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            }
            catch(IOException e){
                // 파일 삭제 과정에서 IOException이 발생할 경우, RuntimeException으로 래핑하여 예외를 던짐
                throw new RuntimeException(e.getMessage());
            }
        });
    }
    
}
