package com.example.mujeans.repository;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.MailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailAuthRepository extends JpaRepository<MailDTO, Integer> {

    //이메일 인증 저장
    MailDTO save(MailDTO mailDTO);

    //이메일 코드 확인
    List<MailDTO> findByEmailAndCodeAndCertifiedYnOrderByCreatedAtDesc(String email, String code, String certifiedYn);
}