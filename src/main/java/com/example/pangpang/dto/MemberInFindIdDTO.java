package com.example.pangpang.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 아이디찾기 DTO

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInFindIdDTO {

    @NotNull(message = "이름은 필수 항목입니다.")
    private String memberNameInFindId;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Size(min = 6, max = 6, message = "생년월일은 숫자 6자리로 입력해주세요.")
    private String memberBirthInFindId;
}
