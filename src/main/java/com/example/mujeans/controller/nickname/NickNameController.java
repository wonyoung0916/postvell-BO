package com.example.mujeans.controller.nickname;

import com.example.mujeans.service.NickNameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Log4j2
@RestController
@RequestMapping("/nick")
@CrossOrigin(origins = "*")
public class NickNameController {

    @Autowired
    private NickNameService nickNameService;

    @ResponseBody
    @PostMapping(value = "/produce", produces = "application/json; charset=UTF-8")
    public String setBoards(HttpServletRequest request) {
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        int code = 500;
        String message = "닉네임 생성중 오류가 발생하였습니다.";
        String nick = "";

        try {
            nick = nickNameService.getNickName();

            // 주고받는 API 형태로 변환
            map.put("RandomNickNm", nick);
            map.put("code", 200);
            map.put("message", "닉네임 생성완료");
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