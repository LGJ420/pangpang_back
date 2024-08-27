package com.example.pangpang.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Notice;
import com.example.pangpang.repository.MemberRepository;
import com.example.pangpang.repository.NoticeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;


    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO) {

    Pageable pageable =
            PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("noticeCreated").descending());

        Page<Notice> result = noticeRepository.findAll(pageable);

        List<NoticeDTO> dtoList = result.getContent().stream()
            .map(notice->modelMapper.map(notice, NoticeDTO.class))
            .collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<NoticeDTO> responseDTO = PageResponseDTO.<NoticeDTO>withAll()
            .dtoList(dtoList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(totalCount)
            .build();

        return responseDTO;
    }



    public NoticeDTO getOne(Long id) {

        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notice not found"));
        NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);

        return noticeDTO;
    }



    public void createOne(Long id, NoticeDTO noticeDTO){

        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (!member.getMemberRole().equals("Admin")){

            throw new RuntimeException("운영자가 아니면 공지사항 등록이 불가능합니다.");
        }

        Notice notice = Notice.builder()
            .noticeTitle(noticeDTO.getNoticeTitle())
            .noticeContent(noticeDTO.getNoticeContent())
            .member(member)
            .build();

        noticeRepository.save(notice);
    }

    
    public void modify(Long memberId, NoticeDTO noticeDTO){

        Notice notice = noticeRepository.findById(noticeDTO.getId())
            .orElseThrow(() -> new EntityNotFoundException("Notice not found"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (!member.getMemberRole().equals("Admin")){

            throw new IllegalArgumentException("관리자만 수정 할 수 있습니다.");
        }

        notice.changeNoticeTitle(noticeDTO.getNoticeTitle());
        notice.changeNoticeContent(noticeDTO.getNoticeContent());
        notice.changeNoticeUpdated(LocalDateTime.now());
        
        noticeRepository.save(notice);
    }


    public void delete(Long memberId, Long noticeId){

        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new EntityNotFoundException("Notice not found"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (!member.getMemberRole().equals("Admin")){

            throw new IllegalArgumentException("관리자만 삭제 할 수 있습니다.");
        }

        noticeRepository.delete(notice);
    }
}
