package com.example.mujeans.repository.member;

import com.example.mujeans.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<MemberDTO, Integer> {

    MemberDTO findByEmail(String email);

    MemberDTO save(MemberDTO memberDTO);

}
