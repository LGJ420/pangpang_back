package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Notice;
import com.example.pangpang.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;


    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO) {

    Pageable pageable =
            PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("noticeCreated").descending());

        Page<Notice> result = noticeRepository.findAll(pageable);

        List<NoticeDTO> dtoList = result.getContent().stream()
            .map(todo->modelMapper.map(todo, NoticeDTO.class))
            .collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<NoticeDTO> responseDTO = PageResponseDTO.<NoticeDTO>withAll()
            .dtoList(dtoList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(totalCount)
            .build();

        return responseDTO;
    }
}
