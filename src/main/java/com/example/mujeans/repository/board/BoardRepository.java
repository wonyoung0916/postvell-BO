package com.example.mujeans.repository.board;

import com.example.mujeans.model.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardDTO, Integer> {
    List<BoardDTO> findAll();
    Page<BoardDTO> findByUseYnOrderByRegDateDesc(String useYn, Pageable pageable);
    List<BoardDTO> findById(int bbsSeq);
    BoardDTO save(BoardDTO boardDTO);
}