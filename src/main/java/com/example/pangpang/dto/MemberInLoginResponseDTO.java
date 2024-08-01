package com.example.pangpang.dto;

import com.example.pangpang.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInLoginResponseDTO {
    private String token;

    private int exprTime;

    private Member member;
}
