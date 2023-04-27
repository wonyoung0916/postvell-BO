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

import java.util.List;

@Log4j2
@Service("SignInService")
@RequiredArgsConstructor
public class SignInService {

    private final SignInRepository signInRepository;

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
    private boolean validateDuplicateMember(MemberDTO member) {
        //true : 회원이 존재함
        //false : 회원이 없음
        return signInRepository.existsByEmail(member.getEmail());
    }
}
