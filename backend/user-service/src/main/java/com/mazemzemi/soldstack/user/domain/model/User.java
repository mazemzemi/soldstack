package com.mazemzemi.soldstack.user.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private UUID id;
    private String username;
    private String email;
}
