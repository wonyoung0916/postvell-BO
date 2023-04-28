package com.example.mujeans.controller.member;

import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.service.member.LoginService;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Log4j2
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    private static final String secretKey = "kjdfhkjhdkjhfQEjtfsdkjfhksa321425"; //AccessToken 체크시 필요한 암호키

    @ResponseBody
    @PostMapping("/loginProc")
    public String loginProc(@ModelAttribute("email") String email, @ModelAttribute("pw") String pw) {
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String code = "500";
        String message = "로그인 정보가 일치하지 않습니다";

        try {
            if(email.equals(null) || email.equals("")) {
                code = "501";
                message = "이메일을 입력해주세요.";
                throw new Exception();
            }
            if(pw.equals(null) || pw.equals("")) {
                code = "502";
                message = "비밀번호를 입력해주세요.";
                throw new Exception();
            }

            MemberDTO mem = loginService.loginChk(email);
            if (mem != null) {
                String enPw = mem.getPw();
                int memSeq = mem.getMemSeq();
                boolean result = loginService.isPwMatch(pw, enPw);
                if (result == true) {
                    String accessToken = createToken(email, memSeq);

                    // 주고받는 API 형태로 변환
                    map.put("code", 200);
                    map.put("message", "로그인 성공");
                    map.put("accessToken", accessToken);

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

    public String createToken(String email, int memSeq) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("memSeq", memSeq);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000)))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
