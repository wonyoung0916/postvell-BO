package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BBS")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bbs_seq;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "NICK_NAME")
    private String nick_name;

    @Column(name = "CREATED_AT")
    private String createdAt;
}
