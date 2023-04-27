package com.example.mujeans.service.member;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MemberService")
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberDTO> getList() {
        return memberRepository.findByUseYn("Y");
    }
}
