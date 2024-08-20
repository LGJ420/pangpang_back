package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
    
}
