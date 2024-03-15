package com.bgod.auth.Service;

import com.bgod.auth.Entity.DTO.AuthDTO;
import com.bgod.auth.Entity.DTO.LoginDTO;
import com.bgod.auth.Entity.Role;
import com.bgod.auth.Entity.User;
import com.bgod.auth.Repository.UserRepository;
import com.bgod.auth.Security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public ResponseEntity register(AuthDTO authDTO){
        String hashedPassword=passwordEncoder.encode(authDTO.getPassword());
        User user= userRepository.getUserByEmail(authDTO.getEmail());
        if(user==null){
            User newUser=new User();
            newUser.setUserName(authDTO.getUserName());
            newUser.setEmail(authDTO.getEmail());
            newUser.setPassword(hashedPassword);
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
            //userDetails modelinde user oluşturup id şifre ve rolleri atıyoruz
            org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                    newUser.getId().toString(),
                    newUser.getPassword(),
                    newUser.getAuthorities()
            );
            String token=jwtToken.generateJwtToken(userDetails);
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.badRequest().body("üzgünüz bu gerçekleşemez kullanıcı zaten kayıtlı");

    }

    public ResponseEntity login(LoginDTO loginDTO) {
        User user=userRepository.getUserByEmail(loginDTO.getEmail());
        if (user!=null){
            if(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
                org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                        user.getId().toString(),
                        user.getPassword(),
                        user.getAuthorities()
                );
                String token=jwtToken.generateJwtToken(userDetails);
                return ResponseEntity.ok(token);
            }
            return ResponseEntity.badRequest().body("email veya şifre yanlış tekrar deneyi");
        }
        return ResponseEntity.badRequest().body("bu emaile kayıtlı bir kullanıcı yok");
    }
}
