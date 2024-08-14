package com.example.pangpang.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 마이페이지에서 내정보변경할때, 비밀번호로 한번 확인하는 DTO

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberMypageComfirmBeforeProfileDTO {

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    private String memberPwInConfirmBeforeProfile;

}
