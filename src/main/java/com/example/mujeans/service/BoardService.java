package com.example.mujeans.service;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.CommentDTO;
import com.example.mujeans.model.LetterDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.repository.board.CommentRepository;
import com.example.mujeans.repository.letter.LetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.stream.events.Comment;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service("boardService")
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LetterRepository letterRepository;
    private final CommentRepository commentRepository;

    public List<BoardDTO> getList(int page) {
        //int page = 0; // 페이지 번호 (0부터 시작)
        int size = 10; // 페이지 당 엔티티 개수
        Pageable pageable = PageRequest.of(page-1, size); // Pageable 객체 생성

        Page<BoardDTO> boardDTOPage = boardRepository.findByUseYnOrderByRegDateDesc("Y", pageable);
        log.info("tqq==========================="+boardDTOPage);
        log.info(boardDTOPage.getContent());
        return boardDTOPage.getContent();
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

    public CommentDTO insertComment(CommentDTO commentDTO){
        List<CommentDTO> list = commentRepository.findByBbsSeq(commentDTO.getBbsSeq());
        int size = list.size();
        commentDTO.setNickNm("무명의 덕후"+size+1);
        return commentRepository.save(commentDTO);
    }

    //230428 원영 (받은 편지 삭제)
    @Transactional
    public boolean deleteLetter(LetterDTO letterDTO, int memSeq){
        //변수설정
        LetterDTO letter = letterRepository.findById(letterDTO.getLetSeq());
        boolean success = false;
        int letMemSeq = letterRepository.findById(letterDTO.getLetSeq()).getMemSeq();

        if (letMemSeq != memSeq){
            return false;
        }

        //letter 데이터 수정
        letter.setUseYn("N");
        log.info(letter.getUseYn());
        log.info(letterDTO.getUseYn());

        if (letter.getUseYn().equals("N")){
            success = true;
        }

        return success;
    }

    public List<LetterDTO> getLetterList(int memSeq){
        return letterRepository.findByUseYnAndMemSeq("Y", memSeq);
    }

    public LetterDTO getLetterDetail(int letSeq){
        return letterRepository.findById(letSeq);
    }

    public List<BoardDTO> getListCnt(){
        return boardRepository.findAll();
    }
}
