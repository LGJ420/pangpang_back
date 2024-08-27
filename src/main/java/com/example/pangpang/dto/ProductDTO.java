package com.example.pangpang.dto;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @NotEmpty
    private String productTitle;

    @NotNull
    private String productContent;

    @Positive
    private int productPrice;

    @NotNull
    private String productDetailContent;

    @NotNull
    private String productCategory;

    @Positive
    private int productStock;

    @Positive
    private int productSales;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime productCreated;


    // 상품에 첨부된 파일들을 나타내는 MultipartFile 객체들의 리스트
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 업로드된 파일 이름들을 나타내는 문자열 리스트
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

}
