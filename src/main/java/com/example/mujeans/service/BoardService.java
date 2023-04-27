package com.example.mujeans.service;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("boardService")
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardDTO> getList() {
         return boardRepository.findByUseYn("Y");
    }

    public BoardDTO insert(BoardDTO boardDTO) {
        return boardRepository.save(boardDTO);
    }
}
