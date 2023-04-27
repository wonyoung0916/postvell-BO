package com.example.mujeans.service;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.LetterDTO;
import com.example.mujeans.model.MemberDTO;
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
import java.util.List;
import java.util.Optional;

@Log4j2
@Service("SignInService")
@RequiredArgsConstructor
public class SignInService {

    private final SignInRepository signInRepository;
    private final EntityManagerFactory entityManagerFactory;

    //230427 주민 (회원가입)
    @Transactional
    public MemberDTO signProc(MemberDTO memberDTO)  {
        boolean isDup;

        //중복회원 검증
        isDup = validateDuplicateMember(memberDTO);

        if (isDup){
            //이메일 중복이 있을경우
            return null;
        }else {
            //이메일 중복이 없을경우
            return signInRepository.save(memberDTO);
        }

    }


    //230427 주민 (중복회원 검증)
    private boolean validateDuplicateMember(MemberDTO memberDTO) {
        //true : 회원이 존재함
        //false : 회원이 없음
        return signInRepository.existsByEmail(memberDTO.getEmail());
    }


    //230427 주민 (비밀번호 초기화)
    @Transactional
    public boolean resetPw(MemberDTO memberDTO){
        //변수정리
        boolean success = false;
        MemberDTO myMember = signInRepository.findByEmail(memberDTO.getEmail());

        //조회 가능한 회원이 없는 경우
        if (myMember == null){
            return success;
        }

        //member데이터 수정
        myMember.setPw(memberDTO.getPw());
        if (myMember.getPw().equals(memberDTO.getPw())){
            success = true;
        }

        return success;
    }
}
