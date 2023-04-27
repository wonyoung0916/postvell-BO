package com.example.mujeans.repository.nick;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.NickDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NickNameRepository extends JpaRepository<NickDTO, Integer> {
    List<NickDTO> findAll();
    List<NickDTO> findByDepth(int depth);
}