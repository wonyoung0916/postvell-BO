package com.example.mujeans.controller.member;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.board.BoardRepository;
import com.example.mujeans.service.member.LoginService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Log4j2
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ResponseBody
    @PostMapping("/loginProc")
    public String loginProc(@ModelAttribute("email") String email, @ModelAttribute("pw") String pw) {
        Gson gson = new Gson();
        HashMap<String,Object> map = new HashMap<>();

        String jsonString = "";
        String code = "500";
        String message = "로그인 정보가 일치하지 않습니다";

        try {
            MemberDTO mem = loginService.loginChk(email);
            if(mem != null) {
                String enPw = mem.getPw();
                boolean result = loginService.isPwMatch(pw,enPw);

                if(result == true) {
                    // 주고받는 API 형태로 변환
                    map.put("code", 200);
                    map.put("message", "로그인 성공");
                } else {
                    throw new Exception();
                }


            } else {
                throw new Exception();
            }

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
