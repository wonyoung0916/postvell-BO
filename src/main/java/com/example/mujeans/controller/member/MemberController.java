package com.example.mujeans.controller.member;

import com.example.mujeans.model.BoardDTO;
import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.service.member.MemberService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ResponseBody
    @GetMapping(value = "/getList", produces = "application/json; charset=UTF-8")
    public String getList(HttpServletRequest request) {
        Gson gson = new Gson();
        HashMap<String,Object> map = new HashMap<>();

        String jsonString = "";
        String code = "500";
        String message = "로그인 정보가 일치하지 않습니다";

        try {
            List<MemberDTO> list = memberService.getList();

            // 주고받는 API 형태로 변환
            map.put("list", list);
            map.put("code", 200);
            map.put("message", "리스트 조회완료");
        } catch (Exception e) {
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
