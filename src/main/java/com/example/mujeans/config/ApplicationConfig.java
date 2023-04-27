package com.example.mujeans.config;

import com.example.mujeans.repository.signIn.SignInRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {ApplicationConfig.class, SignInRepository.class})
public class ApplicationConfig {


}
