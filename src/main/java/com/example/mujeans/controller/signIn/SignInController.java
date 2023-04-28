package com.example.mujeans.controller.signIn;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.service.SignInService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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


    //230427 주민 (회원가입)
    @ResponseBody
    @PostMapping(value = "/signProc",  produces = "application/json; charset=UTF-8")
    public String signProc(MemberDTO memberDTO, String authCode) {
        log.info(" signIn/signProc ====================================>"+ memberDTO + "/ authCode : "+authCode);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();
        Map<String, Object> response = new HashMap<>();

        //확인결과
        boolean isDup = false; // 중복회원 검사 : false - 중복회원 없음 / true - 중복회원 있음
        boolean success = false; // 이메일 인증 검사 : false - 이메일 인증 안됨 / true - 이메일 인증 완료

        String jsonString = "";
        String encodedPw = "";
        int code;
        String message;

        try {

            //비밀번호 암호화
            encodedPw = passwordEncoder.encode(memberDTO.getPw());
            memberDTO.setPw(encodedPw);
            memberDTO.setUseYn("Y");

            response = signInService.signProc(memberDTO, authCode);

            isDup = (boolean) response.get("isDup");
            success = (boolean) response.get("success");

            if (isDup){
                //이메일 중복이 있을경우
                code = 502;
                message = "이미 등록된 이메일입니다.";
            }else if (!success){
                code = 503;
                message = "이메일 검증 코드가 일치하지 않습니다.";
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




    //230427 주민 (비밀번호 초기화)
    @ResponseBody
    @PostMapping(value = "/resetPw",  produces = "application/json; charset=UTF-8")
    public String resetPw(MemberDTO memberDTO, String authCode) {
        log.info(" signIn/resetPw ====================================>"+ memberDTO);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String pw = "";
        String encodedPw = "";
        boolean isDone;
        int code;
        String message;

        try {

            //비밀번호 암호화
            pw = memberDTO.getPw();
            if (pw == null || pw.trim().equals("")){
                code = 503;
                message = "비밀번호를 입력해주세요.";

            }else {

                encodedPw = passwordEncoder.encode(pw);
                memberDTO.setPw(encodedPw);
                memberDTO.setUseYn("Y");

                isDone = signInService.resetPw(memberDTO);
                if (isDone){
                    code = 200;
                    message = "비밀번호 초기화가 완료되었습니다.";
                }else {
                    code = 502;
                    message = "비밀번호 초기화에 실패했습니다.";
                }

            }

        } catch (Exception e) {
            code = 501;
            message = "비밀번호 초기화중 오류가 발생했습니다.";
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