package com.example.ouath2demo.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class LoginResult {

    @NonNull
    private String jwt;
}
