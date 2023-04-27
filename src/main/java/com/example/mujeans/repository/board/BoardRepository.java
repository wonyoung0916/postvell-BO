package com.example.mujeans.repository.board;

import com.example.mujeans.model.BoardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardDTO, Integer> {
    List<BoardDTO> findAll();
    List<BoardDTO> findByUseYn(String useYn);
    List<BoardDTO> findById(int bbsSeq);
    BoardDTO save(BoardDTO boardDTO);
}