package com.example.mujeans.service;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.LetterDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.repository.letter.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("boardService")
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LetterRepository letterRepository;

    public List<BoardDTO> getList() {
         return boardRepository.findByUseYn("Y");
    }

    public BoardDTO insert(BoardDTO boardDTO) {
        return boardRepository.save(boardDTO);
    }

    public LetterDTO insertLetter(LetterDTO letterDTO){
        return letterRepository.save(letterDTO);
    }

    public int getTotalPages(int totalCnt) {
        return (int) Math.ceil((double) totalCnt / 10);
    }

    public List<BoardDTO> getDetail(int bbsSeq){
        return boardRepository.findById(bbsSeq);
    }
}
