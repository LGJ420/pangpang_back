package com.example.pangpang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pangpang.entity.Member;
import com.example.pangpang.repository.MemberRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository MemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember() {
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .memberId("user" + i)
                    .memberPw(passwordEncoder.encode("1111"))
                    .memberBirth(111111)
                    .build();
        }

        for (int i = 0; i < 5; i++) {
            Member member = Member.builder()
                    .role("Admin")
                    .build();
        }

        for (int i = 5; i < 10; i++) {
            Member member = Member.builder()
                    .role("User")
                    .build();
        }
    }

    @Test
    public void testRead(){
        
    }
}
