package com.example.mujeans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors().disable()		//cors방지
                .csrf().disable()		//csrf방지
                .headers().frameOptions().disable()
                .and()
            .authorizeRequests()
                .antMatchers("/board/MyList", "/board/MyDetail").authenticated()// 로그인이 필요한 페이지는 인증된 사용자만 접근 가능
                .anyRequest().permitAll()  // 로그인 페이지와 회원가입 페이지는 인증 없이 접근 가능
                .and()
            .formLogin()
                .loginPage("/login/index") // 로그인 페이지 경로 설정
                .defaultSuccessUrl("/") // 로그인 성공 시 리다이렉트할 페이지 경로 설정
                .and()
            .logout()
                .logoutUrl("/login/logoutProc") // 로그아웃 URL 설정
                .logoutSuccessUrl("/login/index") // 로그아웃 성공 시 리다이렉트할 페이지 경로 설정
                .invalidateHttpSession(true);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
