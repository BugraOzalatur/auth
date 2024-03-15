package com.bgod.auth.Service;

import com.bgod.auth.Entity.DTO.DeleteUserDTO;
import com.bgod.auth.Entity.DTO.PasswordDTO;
import com.bgod.auth.Entity.DTO.UpdateDTO;
import com.bgod.auth.Entity.DTO.UserDTO;
import com.bgod.auth.Entity.User;
import com.bgod.auth.Repository.UserRepository;
import com.bgod.auth.Security.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService   {

    @Autowired
    private JwtToken jwtToken;
@Autowired
private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

public ResponseEntity updateUser(UpdateDTO updateDTO){
UUID id=updateDTO.getId();
    User user=userRepository.getUserById(id);
if(user == null){
    return ResponseEntity.badRequest().body("böyle bir kullanıcı yok");
}
user.setUserName(updateDTO.getUserName());
if(userRepository.getUserByEmail(updateDTO.getEmail())== null){
    user.setEmail(updateDTO.getEmail());
}
    userRepository.save(user);
UserDTO updatedUserDTO=this.convertToDTO(user);
return ResponseEntity.ok(updatedUserDTO);
}

public ResponseEntity deleteUser(DeleteUserDTO deleteUserDTO){
    UUID userId=deleteUserDTO.getId();
    User user=userRepository.getUserById(userId);
    if(user != null){
        userRepository.delete(user);
        return ResponseEntity.ok("işlem başarılı");
    }
return ResponseEntity.badRequest().body("böyle bir kullanıcı yok");
}
public ResponseEntity changePassword( PasswordDTO passwordDTO){
   String changedPassword= passwordDTO.getChangedPassword();
   String inDTOPasword=passwordDTO.getFirstPassword();

   //UUID userId=UUID.fromString(id);
    User user=userRepository.getUserById(passwordDTO.getId());
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
        User user =userRepository.getUserById(id);
        UserDTO signedUser=this.convertToDTO(user);
        return ResponseEntity.ok(signedUser);

    }
    public ResponseEntity getAllUser() {
    List<UserDTO> users= userRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(users);

    }
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

}
