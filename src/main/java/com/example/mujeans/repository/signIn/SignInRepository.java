package com.example.mujeans.repository.signIn;

import com.example.mujeans.model.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public interface SignInRepository extends JpaRepository<MemberDTO, Integer> {

     //230427 주민 (회원가입)
     MemberDTO save(MemberDTO memberDTO);

     //230427 주민 (중복회원 검증)
     boolean existsByEmail(String email);

}
