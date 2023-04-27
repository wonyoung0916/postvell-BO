package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBERS")
@Getter @Setter
@ToString
@Component
@Builder
@AllArgsConstructor
public class MemberDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mem_seq;

    @Column(name = "MEM_EMAIL")
    private String email;

    @Column(name = "MEM_NAME")
    private String name;

    @Column(name = "MEM_PW")
    private String pw;

    @Column(name = "CREATED_AT")
    private String createdAt;

    public MemberDTO(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now.toString();
    }

}
