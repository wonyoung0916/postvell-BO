package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LETTER")
@Getter @Setter
@ToString
@Component
@Builder
@AllArgsConstructor
public class LetterDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int letSeq;

    @Column(name = "MEM_SEQ")
    private int memSeq;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_AT")
    private String regDate;

    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "NICK_NAME")
    private String nickNm;

    @Column(name = "IMAGE_PATH")
    private String image;

    public LetterDTO(){
        this.useYn = "Y";
        LocalDateTime now = LocalDateTime.now();
        this.regDate = now.toString();
    }
}
