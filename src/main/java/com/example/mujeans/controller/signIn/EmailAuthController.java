package com.example.mujeans.controller.signIn;

import com.example.mujeans.model.MailDTO;
import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.service.EmailAuthService;
import com.example.mujeans.service.SignInService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Log4j2
@RestController
@RequestMapping("/emailAuth")
@CrossOrigin(origins = "*")
public class EmailAuthController {

    @Autowired
    private EmailAuthService emailAuthService;


    //230427 주민 (이메일 발송)
    /*
    @ResponseBody
    @PostMapping(value = "/sendEmail",  produces = "application/json; charset=UTF-8")
    public String sendEmail(String email) {
        log.info(" emailAuth/sendEmail ====================================>"+ email);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String authCode = ""; //이메일 인증코드
        int code = 500;
        String message = "이메일 인증에 실패했습니다.";

        try {

            MailDTO mailDTO = new MailDTO();
            mailDTO.setEmail(email);
            mailDTO.setTitle("[뮤진스] 이메일 인증 번호입니다.");

            //emailAuthService.mailSend(mailDTO);
            authCode = emailAuthService.sendSimpleMessage(mailDTO);
            if (authCode != null && !authCode.equals("")){
                code = 200;
                message = "이메일 인증이 완료되었습니다.";
            }


        } catch (Exception e) {
            code = 501;
            message = "이메일 전송중 오류가 발생했습니다.";
            log.error("Exception::"+e.getMessage());
            e.printStackTrace();
        }

        //json
        map.put("code", code);
        map.put("message", message);
        jsonString = gson.toJson(map);

        return jsonString;
    }

     */

    @ResponseBody
    @PostMapping(value = "/sendEmail",  produces = "application/json; charset=UTF-8")
    public void sendEmail(String email) {
        log.info(" emailAuth/sendEmail ====================================>"+ email);
        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String authCode = ""; //이메일 인증코드
        int code = 500;
        String message = "이메일 인증에 실패했습니다.";

        try {

            MailDTO mailDTO = new MailDTO();
            mailDTO.setEmail(email);
            mailDTO.setTitle("[뮤진스] 이메일 인증 번호입니다.");

            //emailAuthService.mailSend(mailDTO);
            emailAuthService.sendSimpleMessage(mailDTO);


        } catch (Exception e) {
            log.error("Exception::"+e.getMessage());
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/certified")
    public String certified(HttpSession session, HttpServletRequest request,
                          MailDTO mailDTO) throws MessagingException {
        log.info(" emailAuth/certified ====================================>"+ mailDTO);

        // 변수 초기화
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();
        boolean certified;
        int code;
        String message;

        try {
            certified = emailAuthService.certified(mailDTO);
            log.info("certified:::"+certified);
            if (certified){
                code = 200;
                message = "인증에 성공했습니다.";
            }else {
                code = 502;
                message = "인증에 실패했습니다.";
            }

        }catch (Exception e){
            code = 501;
            message = "이메일 전송중 오류가 발생했습니다.";
            e.printStackTrace();
        }

        //json
        map.put("code", code);
        map.put("message", message);

        return gson.toJson(map);
    }

    /*
    // 인증 메일 인증하기 버튼
    @RequestMapping("/emailAuthUpdate")
    public void emailAuthUpdate(HttpServletResponse response,
                                HttpServletRequest request,
                                int ectSeq,
                                String usrId) {

        log.info("usrId : " + usrId);
        log.info("ectSeq : " + ectSeq);

        // 변수 선언
        int validCode = 0;

        try {
            // 이메일 인증
            HashMap<String, Object> validMap = new HashMap<>();
            validMap = emailAuthService.validToken(ectSeq, usrId);
            validCode = (int)validMap.get("validCode");

            // 이메일 인증 결과 별 분기
            if (validCode == 1) {
                ScriptUtils.alert(response, "인증 되었습니다.");
            } else if(validCode == 2) {
                ScriptUtils.alert(response, "인증 유효 시간이 만료되었습니다. 다시 시도해주세요.");
            } else if(validCode == 3){
                ScriptUtils.alert(response, "이미 인증된 메일입니다.");
            } else if(validCode == 4){
                ScriptUtils.alert(response, "만료된 메일입니다. 다시 시도해주세요.");
            } else {
                ScriptUtils.alert(response, "문제가 발생했습니다. 다시 시도해주세요.");
            }
        } catch(Exception e){
            log.info("exception 발생!");
            e.printStackTrace();
        }
    }


    // 인증 상태 체크
    @ResponseBody
    @PostMapping("/chkEmailCertified")
    public String chkEmailCertified(int ectSeq) {
        log.info("/sign/chkEmailCertified");
        log.info("ectSeq : " + ectSeq);

        // 변수 선언
        int result = 0;

        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        try {
            int row = emailAuthService.chkEmailCertified(ectSeq);
            log.info("성공한 row : " + row);

            if(row > 0) {
                result = 1;
            }

            // 인증 상태 확인에 성공하는 경우
            if (result == 1) {
                map.put("code", 200);
                map.put("msg", "인증 되었습니다.");
            } else {
                map.put("code", 500);
                map.put("msg", "인증되지 않았습니다. 이메일을 확인해 주세요.");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        String jsonString = gson.toJson(map);
        return jsonString;
    }

     */



}