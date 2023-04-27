package com.example.mujeans.repository.member;

import com.example.mujeans.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberDTO, Integer> {
    List<MemberDTO> findByUseYn(String useYn);
}