package com.example.mujeans.controller.board;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.CommentDTO;
import com.example.mujeans.model.LetterDTO;
import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.service.BoardService;
import com.example.mujeans.service.CommentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
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

    private static final String secretKey = "kjdfhkjhdkjhfQEjtfsdkjfhksa321425"; //AccessToken 체크시 필요한 암호키

    @ResponseBody
    @GetMapping(value = "/list",  produces = "application/json; charset=UTF-8")
    public String getAllBoards(@RequestParam(name = "page", required = false) int page) {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "게시글 조회중 오류가 발생하였습니다.";
        int totalCnt = 0;
        int totalPage = 0;

        try {

            List<BoardDTO> list = boardService.getList(page);
            //Collections.reverse(list);
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

    @ResponseBody
    @PostMapping(value = "/addReply", produces = "application/json; charset=UTF-8")
    public String setComments(CommentDTO commentDTO) {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "댓글 등록중 오류가 발생하였습니다.";

        try {
            boardService.insertComment(commentDTO);

            // 주고받는 API 형태로 변환
            map.put("code", 200);
            map.put("message", "댓글 등록완료");
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
    @PostMapping(value = "/deleteLetter", produces = "application/json; charset=UTF-8")
    public String deleteLetter(@Param("accessToken") String accessToken, LetterDTO letterDTO) {
        log.info(" boards/deleteLetter ====================================>"+ letterDTO);
        log.info("accessToken ====================================>"+accessToken);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        //member
        String email;
        int memSeq;

        //result
        String jsonString = "";
        int code = 500;
        String message = "편지 삭제중 오류가 발생하였습니다.";
        boolean isDone;

        try {

            //토큰으로 멤버시퀀스 받아오기
/*            Claims memInfo = getClaims(accessToken);
            memSeq = (int) memInfo.get("memSeq");
            log.info("memSeq >>>>>>>>>>>>>>>>> " + memSeq);*/
            /*
            if (session.getAttribute("memSeq") == null){
                throw new Exception("===== deleteLetter session null");
            }*/

            //memSeq = (int)session.getAttribute("ssMemSeq");

           isDone = boardService.deleteLetter(letterDTO);
            if (isDone){
                code = 200;
                message = "편지 삭제가 완료되었습니다.";
            }else {
                code = 502;
                message = "편지 삭제에 실패했습니다.";
            }

        } catch (Exception e) {
            code = 501;
            message = "편지 삭제중 오류가 발생했습니다.";
            e.printStackTrace();
        }

        //json
        map.put("code", code);
        map.put("message", message);

        jsonString = gson.toJson(map);

        return jsonString;
    }

    // 토큰 복호화
    public Claims getClaims(String accessToken) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        return body;
    }

    public Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @ResponseBody
    @GetMapping(value = "/letterList",  produces = "application/json; charset=UTF-8")
    public String letterList(HttpSession session) {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "나의 우편함 조회중 오류가 발생하였습니다.";

        try {
            /*
            if (session.getAttribute("memSeq") == null){
                throw new Exception("===== deleteLetter session null");
            }*/

            //memSeq = (int)session.getAttribute("ssMemSeq");


            List<LetterDTO> list = boardService.getLetterList();
            Collections.reverse(list);
            // 주고받는 API 형태로 변환
            map.put("list", list);
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
    @GetMapping(value = "/letterDetail",  produces = "application/json; charset=UTF-8")
    public String letterDetail(@Param("letSeq") int letSeq) {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "게시글 조회중 오류가 발생하였습니다.";

        try {
            LetterDTO detail = boardService.getLetterDetail(letSeq);
            // 주고받는 API 형태로 변환
            map.put("list", detail);
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

}