package com.example.mujeans.controller.board;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.CommentDTO;
import com.example.mujeans.model.LetterDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.service.BoardService;
import com.example.mujeans.service.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/boards")
@CrossOrigin(origins = "*")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @GetMapping(value = "/list",  produces = "application/json; charset=UTF-8")
    public String getAllBoards(@Param("page")int page) {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "게시글 조회중 오류가 발생하였습니다.";
        int totalCnt = 0;
        int totalPage = 0;

        try {
            List<BoardDTO> list = boardService.getList();
            totalCnt = list.size();
            totalPage = boardService.getTotalPages(totalCnt);

            // 주고받는 API 형태로 변환
            map.put("list", list);
            map.put("code", 200);
            map.put("message", "리스트 조회완료");
            map.put("totalPage", totalPage);
        } catch (Exception e) {
            map.put("code", code);
            map.put("message", message);
            e.printStackTrace();

        } finally {
            //json
            jsonString = gson.toJson(map);

            return jsonString;
        }
    }

    @ResponseBody
    @GetMapping(value = "/detail",  produces = "application/json; charset=UTF-8")
    public String getDetail(@Param("bbsSeq") int bbsSeq) {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "게시글 조회중 오류가 발생하였습니다.";

        try {
            List<BoardDTO> detail = boardService.getDetail(bbsSeq);
            List<CommentDTO> commentList = commentService.getList(bbsSeq);
            // 주고받는 API 형태로 변환
            map.put("list", detail);
            map.put("replytList", commentList);
            map.put("code", 200);
            map.put("message", "리스트 조회완료");
        } catch (Exception e) {
            map.put("code", code);
            map.put("message", message);
            e.printStackTrace();

        } finally {
            //json
            jsonString = gson.toJson(map);

            return jsonString;
        }
    }

    @ResponseBody
    @PostMapping(value = "/regist", produces = "application/json; charset=UTF-8")
    public String setBoards(BoardDTO boardDTO, LetterDTO letterDTO, HttpServletRequest request) {
        log.info("boardDTO====================================>"+ boardDTO);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "게시글 등록중 오류가 발생하였습니다.";

        try {
            if (boardDTO.getVellYn().equals(true)){
                boardService.insert(boardDTO);
            }else{
                boardService.insertLetter(letterDTO);
            }

            // 주고받는 API 형태로 변환
            map.put("code", 200);
            map.put("message", "게시글 등록완료");
        } catch (Exception e) {
            map.put("code", code);
            map.put("message", message);
            e.printStackTrace();
        } finally {
            //json
            jsonString = gson.toJson(map);

            return jsonString;
        }
    }
}