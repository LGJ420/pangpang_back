package com.example.pangpang.dto;

import com.example.pangpang.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class MemberResponseDTO<Member> {
    private boolean result;

    private String message;

    private Member data;

    public static <Member> MemberResponseDTO<Member> setSuccess(String message) {
        return MemberResponseDTO.set(true, message, null);
    }

    public static <Member> MemberResponseDTO<Member> setFailed(String message) {
        return MemberResponseDTO.set(false, message, null);
    }

    public static <Member> MemberResponseDTO<Member> setSuccessData(String message, Member data) {
        return MemberResponseDTO.set(true, message, data);
    }

    public static <Member> MemberResponseDTO<Member> setFailedData(String message, Member data) {
        return MemberResponseDTO.set(false, message, data);
    }
}
