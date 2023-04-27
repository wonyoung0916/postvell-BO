package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "NICKNAME")
@Getter @Setter
@ToString
@Component
@Builder
@AllArgsConstructor
public class NickDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nicSeq;

    @Column(name = "DEPTH")
    private int depth;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_AT")
    private String createdAt;

    public NickDTO() {

    }
}
