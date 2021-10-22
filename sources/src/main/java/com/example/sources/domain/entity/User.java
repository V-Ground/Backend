package com.example.sources.domain.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;

    /**
     * 파라미터로 들어온 비밀번호와 가 비밀번호와 동일한지 검증한다.
     *
     * @param password : 요청으로 들어온 비밀번호
     * @param passwordEncoder : 암호화 tool
     * @return 비밀번호 matching 성공 여부
     */
    public boolean authenticate(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

}
