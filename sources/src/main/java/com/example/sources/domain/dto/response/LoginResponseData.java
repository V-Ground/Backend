package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseData {
    private Long id;
    private String email;
    private String username;
    private String role;
}
