package com.bgod.auth.Entity.DTO;

import com.bgod.auth.Entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String userName;
private String email;
private Role role;
}
