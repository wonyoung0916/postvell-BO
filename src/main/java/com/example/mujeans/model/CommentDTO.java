package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENT")
@Getter @Setter
@ToString
@Component
@Builder
@AllArgsConstructor
public class CommentDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comSeq;

    @Column(name = "BBS_SEQ")
    private int bbsSeq;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "NICK_NAME")
    private String nickNm;

    @Column(name = "CREATED_AT")
    private String regDate;

    public CommentDTO(){
        LocalDateTime now = LocalDateTime.now();
        this.regDate = now.toString();
    }

}
