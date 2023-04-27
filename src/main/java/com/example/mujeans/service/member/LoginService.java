package com.example.mujeans.service.member;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.repository.member.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Log4j2
@Service("loginService")
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberDTO loginChk(String email) {
        return loginRepository.findByEmail(email);
    }

    public boolean isPwMatch(String pw, String enPw) {
        return passwordEncoder.matches(pw, enPw);
    }
}
