package com.example.mujeans.repository.board;

import com.example.mujeans.model.CommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentDTO, Integer> {
    List<CommentDTO> findByBbsSeq(int bbsSeq);
    //BoardDTO save(BoardDTO boardDTO);
}