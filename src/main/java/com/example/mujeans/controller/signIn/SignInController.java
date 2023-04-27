package com.example.mujeans.controller.signIn;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.service.SignInService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Log4j2
@RestController
@RequestMapping("/signIn")
@CrossOrigin(origins = "*")
public class SignInController {

    @Autowired
    private SignInService signInService;
    private PasswordEncoder passwordEncoder;

    public SignInController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseBody
    @PostMapping(value = "/signProc",  produces = "application/json; charset=UTF-8")
    public String signProc(MemberDTO memberDTO) {
        log.info(" signIn/signProc ====================================>"+ memberDTO);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String encodedPw = "";
        int code;
        String message;

        try {

            //비밀번호 암호화
            encodedPw = passwordEncoder.encode(memberDTO.getPw());
            memberDTO.setPw(encodedPw);
            memberDTO.setUseYn("Y");

            MemberDTO resultDto = signInService.signProc(memberDTO);
            if (resultDto == null){
                //이메일 중복이 있을경우
                code = 502;
                message = "이미 등록된 이메일입니다.";
            }else {
                code = 200;
                message = "회원가입에 성공했습니다.";
            }

        } catch (Exception e) {
            code = 501;
            message = "회원가입중 오류가 발생했습니다.";
            log.error("Exception::"+e.getMessage());
            e.printStackTrace();
        }

        //json
        map.put("code", code);
        map.put("message", message);
        jsonString = gson.toJson(map);

        return jsonString;
    }


}