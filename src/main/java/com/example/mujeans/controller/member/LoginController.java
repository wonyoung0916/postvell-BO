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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
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
    private static final String ALGORITHM = "AES"; //AES 알고리즘
    private static final String KEY = "678swrFRKFid5sjf"; // AES 16자리 이하의 시크릿 키
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
                    String accessToken = createToken(email);
                    String enEmail = aesEncrypt(email);
                    // 주고받는 API 형태로 변환
                    map.put("code", 200);
                    map.put("message", "로그인 성공");
                    map.put("accessToken", accessToken);
                    map.put("enEmail", enEmail);
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


    public String createToken(String email){
        Claims claims = Jwts.claims();
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (60*60*24*1000)))
                .signWith(getSigninKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 문자열을 AES-128로 암호화하는 메소드
    public String aesEncrypt(String email) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedValue = cipher.doFinal(email.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedValue);
    }
}
