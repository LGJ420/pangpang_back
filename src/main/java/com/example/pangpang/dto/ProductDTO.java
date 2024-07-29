package com.example.pangpang.dto;

import java.util.*;
import org.springframework.web.multipart.MultipartFile;

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

    // @Builder.Default
    // private List<MultipartFile> files = new ArrayList<>();

    // @Builder.Default
    // private List<String> uploadFileNames = new ArrayList<>();

}
