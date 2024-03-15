package com.bgod.auth.Service;

import com.bgod.auth.Entity.DTO.DeleteUserDTO;
import com.bgod.auth.Entity.DTO.PasswordDTO;
import com.bgod.auth.Entity.DTO.UpdateDTO;
import com.bgod.auth.Entity.User;
import com.bgod.auth.Repository.UserRepository;
import com.bgod.auth.Security.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService   {

    @Autowired
    private JwtToken jwtToken;
@Autowired
private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

public ResponseEntity updateUser(UpdateDTO updateDTO){
UUID id=UUID.fromString(updateDTO.getId());
    User user=userRepository.getUserById(id);
if(user == null){
    return ResponseEntity.badRequest().body("böyle bir kullanıcı yok");
}
user.setUserName(updateDTO.getUserName());
if(userRepository.getUserByEmail(updateDTO.getEmail())== null){
    user.setEmail(updateDTO.getEmail());
}
return ResponseEntity.ok(userRepository.save(user));
}

public ResponseEntity deleteUser(DeleteUserDTO deleteUserDTO){
    UUID userId=UUID.fromString(deleteUserDTO.getId());
    User user=userRepository.getUserById(userId);
    if(user != null){
        userRepository.delete(user);
        return ResponseEntity.ok("işlem başarılı");
    }
return ResponseEntity.badRequest().body("böyle bir kullanıcı yok");
}
public ResponseEntity changePassword(String id, PasswordDTO passwordDTO){
   String changedPassword= passwordDTO.getChangedPassword();
   String inDTOPasword=passwordDTO.getFirstPassword();

   UUID userId=UUID.fromString(id);
    User user=userRepository.getUserById(userId);
    if(user != null){
String currentPassword=user.getPassword();
if(passwordEncoder.matches(inDTOPasword,currentPassword)){
if(!passwordEncoder.matches(changedPassword,currentPassword)){
String hashedPassword=passwordEncoder.encode(changedPassword);
    user.setPassword(hashedPassword);
    //userDetails modelinde user oluşturup id şifre ve rolleri atıyoruz

    return ResponseEntity.ok(userRepository.save(user));
}
    return ResponseEntity.badRequest().body("iki şifre aynı olamaz");

}return ResponseEntity.badRequest().body("şifreniz uyuşmuyor");
    }return ResponseEntity.badRequest().body("kullanıcı yok");
}
    public ResponseEntity getSignedProfile(  @NonNull HttpServletRequest request ){
        UUID id= jwtToken.getSignedProfile(request);
        if(id==null){
            return ResponseEntity.badRequest().body("böyle bir kullanıcı yok");

        }
        return ResponseEntity.ok(userRepository.getUserById(id));

    }
    public ResponseEntity getAllUser() {
    return ResponseEntity.ok(userRepository.findAll());

    }
}
