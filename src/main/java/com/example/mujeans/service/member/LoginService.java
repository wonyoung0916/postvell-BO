package com.example.mujeans.service.member;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.repository.member.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
