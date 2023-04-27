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
    private String createdAt;

    public LetterDTO(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now.toString();
    }

}
