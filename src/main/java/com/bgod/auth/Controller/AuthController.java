package com.bgod.auth.Controller;


import com.bgod.auth.Entity.DTO.AuthDTO;
import com.bgod.auth.Entity.DTO.LoginDTO;
import com.bgod.auth.Entity.Role;
import com.bgod.auth.Entity.User;
import com.bgod.auth.Repository.UserRepository;
import com.bgod.auth.Security.JwtToken;
import com.bgod.auth.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/Auth")
public class AuthController {
   @Autowired
    private AuthService authService;


@PostMapping("/signin")
   public ResponseEntity register(@RequestBody AuthDTO authDTO){
       return authService.register(authDTO);
   }

    @PostMapping("/signup")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
    }

}
