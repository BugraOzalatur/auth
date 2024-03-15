package com.bgod.auth.Controller;

import com.bgod.auth.Entity.DTO.DeleteUserDTO;
import com.bgod.auth.Entity.DTO.PasswordDTO;
import com.bgod.auth.Entity.DTO.UpdateDTO;
import com.bgod.auth.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;
@PutMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody UpdateDTO updateDTO){
        return userService.updateUser(updateDTO);
    }
    @GetMapping("/getUser")
    public ResponseEntity getAllUser(){
    return userService.getAllUser();
    }
    @DeleteMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestBody DeleteUserDTO id){
    return userService.deleteUser(id);
    }
    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody String id,@RequestBody PasswordDTO passwordDTO){
        return userService.changePassword(id,passwordDTO);
    }
    @GetMapping("/userProfile")
    public ResponseEntity userProfile(@NonNull HttpServletRequest request){
    return userService.getSignedProfile(request);
    }
}
