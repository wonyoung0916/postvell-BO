package com.example.mujeans.service;

import com.example.mujeans.model.CommentDTO;
import com.example.mujeans.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commentService")
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    public List<CommentDTO> getList(int bbsSeq){
        return commentRepository.findByBbsSeq(bbsSeq);
    }
}
