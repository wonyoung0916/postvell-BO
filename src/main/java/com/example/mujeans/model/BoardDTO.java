package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BBS")
@Getter @Setter
@ToString
@Component
@Builder
@AllArgsConstructor
public class BoardDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bbsSeq;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "NICK_NAME")
    private String nickNm;

    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "CREATED_AT")
    private String regDate;

    @Transient
    private Boolean vellYn;

    public BoardDTO(){
        this.useYn = "Y";
        LocalDateTime now = LocalDateTime.now();
        this.regDate = now.toString();
    }

}
