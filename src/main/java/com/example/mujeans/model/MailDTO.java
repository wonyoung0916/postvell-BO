package com.example.mujeans.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMAIL_AUTH")
@Getter @Setter
@ToString
@Component
@Builder
@AllArgsConstructor
public class MailDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emaSeq;

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CODE")
    private String code;
    @Column(name = "CERTIFIED_YN")
    private String certifiedYn;
    @Column(name = "EXPIRED_YN")
    private String expiredYn;
    @Column(name = "CREATED_AT")
    private String createdAt;

    private String title;
    private String message;

    public MailDTO() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now.toString();
        this.certifiedYn = "N";
    }
}
