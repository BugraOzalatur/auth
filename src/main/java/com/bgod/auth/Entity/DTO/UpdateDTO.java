package com.bgod.auth.Entity.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateDTO {
    private String email;
    private String userName;
    private String id;
}
