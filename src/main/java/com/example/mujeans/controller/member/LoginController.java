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
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
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
    private static final String KEY = "mysecretkey12345"; // AES 16자리 이하의 시크릿 키

    @ResponseBody
    @PostMapping("/loginProc")
    public String loginProc(HttpSession session, HttpServletRequest request, @ModelAttribute("email") String email, @ModelAttribute("pw") String pw) {
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();

        String jsonString = "";
        String code = "500";
        String message = "로그인 정보가 일치하지 않습니다";

        //세션 초기화
        session.invalidate();
        session = request.getSession(true);

        try {
            MemberDTO mem = loginService.loginChk(email);
            log.info("mem >>>>>>>>>>>>>>>>>>>>>>>> " + mem);
            if (mem != null) {
                String enPw = mem.getPw();
                int memSeq = mem.getMemSeq();
                boolean result = loginService.isPwMatch(pw, enPw);
                log.info("result >>>>>>>>>>>>>>>>>>>>>>>> " + result);
                if (result == true) {
                    String accessToken = createToken(email, memSeq);


//                    // IV 생성
//                    SecureRandom random = new SecureRandom();
//                    byte[] ivBytes = new byte[16];
//                    random.nextBytes(ivBytes);
//                    IvParameterSpec iv = new IvParameterSpec(ivBytes);
//
//                    // SecretKeySpec 객체 생성
//                    //SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
//
//                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
//                    kgen.init(128);
//                    SecretKey skey = kgen.generateKey();
//
//                    SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
//                    Cipher cipher = Cipher.getInstance("AES");
//                    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//
//                    // 데이터 암호화
//                    byte[] encryptedBytes = cipher.doFinal(email.getBytes());
//
//                    System.out.println("encrypted string: " + Hex.encode(encryptedBytes));
//
//                    // 데이터 복호화
//                    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//                    byte[] original = cipher.doFinal(encryptedBytes);
//                    String originalString = new String(original);
//                    log.info("originalString:::" + originalString);
//
//                    // Cipher 객체 생성 및 초기화
//                    //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//                    //cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
//
//
//                    // 암호화된 데이터와 IV 값을 Base64로 인코딩
//                    //String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
//                    //String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);
//
////                    String enEmail = aesEncrypt(email);
//                    log.info("encryptedBase64 >>>>>>>>>>>>>>>>>>>>>>>> " + encryptedBytes);
//                    //log.info("ivBase64 >>>>>>>>>>>>>>>>>>>>>>>> " + ivBase64);
////                    log.info("deEmail >>>>>>>>>>>>>>>>>>>>>>>> " + decrypt(enEmail));
                    // 주고받는 API 형태로 변환
                    map.put("code", 200);
                    map.put("message", "로그인 성공");
                    map.put("accessToken", accessToken);
//                    map.put("enEmail", encryptedBytes);
//                    map.put("skey", skey);
//                    map.put("iv", iv);
                    //map.put("iv", ivBase64);

                    session.setAttribute("ssMemSeq", mem.getMemSeq());
                    session.setAttribute("ssEmail", mem.getEmail());

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

    // 문자열을 AES-128로 암호화하는 메소드
//    public String aesEncrypt(String email) throws Exception {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//        byte[] encryptedValue = cipher.doFinal(email.getBytes(StandardCharsets.UTF_8));
//        return Base64.getEncoder().encodeToString(encryptedValue);
//    }
//
//    public static String decrypt(String enEmail) throws Exception {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(enEmail));
//        return new String(decryptedBytes, StandardCharsets.UTF_8);
//    }

//    public IvParameterSpec iv() {
//        // IV 생성
//        SecureRandom random = new SecureRandom();
//        byte[] ivBytes = new byte[16];
//        random.nextBytes(ivBytes);
//        IvParameterSpec iv = new IvParameterSpec(ivBytes);
//        return iv;
//    }


    @GetMapping("/logoutProc")
    public String logoutProc(HttpSession session) {

        session.invalidate();

        // 리다이렉트 처리
        return "redirect:/";
    }

}
