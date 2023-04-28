package com.example.mujeans.service;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.LetterDTO;
import com.example.mujeans.model.MailDTO;
import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.EmailAuthRepository;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.repository.letter.LetterRepository;
import com.example.mujeans.repository.signIn.SignInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service("SignInService")
@RequiredArgsConstructor
public class SignInService {

    private final SignInRepository signInRepository;
    private final EntityManagerFactory entityManagerFactory;

    private final EmailAuthRepository emailAuthRepository;

    //230427 주민 (회원가입)
    @Transactional
    public Map signProc(MemberDTO memberDTO, String authCode)  {
        boolean isDup;
        boolean success = false;
        MemberDTO member = null;
        Map<String, Object> resultMap = new HashMap<>();
        MailDTO latestMail;

        //중복회원 검증
        isDup = validateDuplicateMember(memberDTO);

        //이메일 인증
        List<MailDTO> list = emailAuthRepository.findByEmailAndCodeAndCertifiedYnOrderByCreatedAtDesc(memberDTO.getEmail(), authCode, "N");

        if (list.size() > 0){
            latestMail = list.get(0);
            log.info("이메일 인증 DTO :: "+latestMail);
            latestMail.setCertifiedYn("Y");
            success = true;
        }

        if (!isDup && success){
            //이메일 중복이 없을경우 & 인증이 완료된 경우
            member = signInRepository.save(memberDTO);
        }

        resultMap.put("isDup", isDup);
        resultMap.put("success", success);
        resultMap.put("member", member);

        return resultMap;

    }


    //230427 주민 (중복회원 검증)
    private boolean validateDuplicateMember(MemberDTO memberDTO) {
        //true : 회원이 존재함
        //false : 회원이 없음
        return signInRepository.existsByEmail(memberDTO.getEmail());
    }


    //230427 주민 (비밀번호 초기화)
    @Transactional
    public boolean resetPw(MemberDTO memberDTO, String authCode){
        //변수정리
        boolean success = false;
        boolean isReset = false;
        MemberDTO myMember = signInRepository.findByEmail(memberDTO.getEmail());
        MailDTO latestMail;

        //조회 가능한 회원이 없는 경우
        if (myMember == null){
            return isReset;
        }

        //이메일 인증
        List<MailDTO> list = emailAuthRepository.findByEmailAndCodeAndCertifiedYnOrderByCreatedAtDesc(memberDTO.getEmail(), authCode, "N");

        if (list.size() > 0){
            latestMail = list.get(0);
            log.info("이메일 인증 DTO :: "+latestMail);
            latestMail.setCertifiedYn("Y");
            success = true;
        }

        //member데이터 수정
        myMember.setPw(memberDTO.getPw());
        if (myMember.getPw().equals(memberDTO.getPw())){
            isReset = true;
        }

        return (success && isReset);
    }
}
