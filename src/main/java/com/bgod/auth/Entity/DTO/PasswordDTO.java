package com.bgod.auth.Entity.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class PasswordDTO {
private String firstPassword;
private String changedPassword;
private UUID id;

}
