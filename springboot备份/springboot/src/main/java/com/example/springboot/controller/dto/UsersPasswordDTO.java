package com.example.springboot.controller.dto;


import lombok.Data;

@Data
public class UsersPasswordDTO {
    private String account;
    private String mail;
    private String cypher;
    private String newCypher;
}
