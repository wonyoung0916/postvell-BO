package com.example.mujeans.controller.board;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.repository.board.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping(value = "/list")
    public String getAllBoards() {

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String code = "500";
        String message = "input 파일 관리 중 오류가 발생하였습니다.";


        try {
            /*if (session == null || session.equals("")) {
                code = "501";
                message = "필수 입력 항목이 누락되었습니다. 세션";
                throw new Exception();
            }*/

            List<BoardDTO> list = boardRepository.findAll();

            // 주고받는 API 형태로 변환
            map.put("list", list);
            map.put("code", 200);
            map.put("message", "리스트 조회완료");
        } catch (
                Exception e) {
            map.put("code", code);
            map.put("message", message);
            map.put("msg", e.getMessage());

        } finally {
            //json
            jsonString = gson.toJson(map);

            return jsonString;
        }
    }
}