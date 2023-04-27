package com.example.mujeans.repository.member;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<MemberDTO, Integer> {

//    int findByEmail(String email);
//    int findByEmailPw(String email, String pw);
    MemberDTO findByEmail(String email);


}
