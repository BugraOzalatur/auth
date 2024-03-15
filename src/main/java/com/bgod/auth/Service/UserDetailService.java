package com.bgod.auth.Service;

import com.bgod.auth.Entity.User;
import com.bgod.auth.Repository.UserRepository;
import com.bgod.auth.Security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailService implements UserDetailsService {
//javanın içindeki usera entegre etme ve şifre rol çekmek için kullanmış olduğumuz service UserServicete yazılabilirdi ama daha detaylı olmasını istedim
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtToken jwtToken;
    private User getUser(String token){
        UUID id = UUID.fromString(jwtToken.extractUserId(token));
        User user=userRepository.getUserById(id);
        return user;

    }
    public String getPasswordAndRole(String token,Boolean x){
        User user= getUser(token);
        if(x==true) {
            String password= user.getPassword().toString();
            return password;
        }
        else{
            return user.getRole().name();
        }

    }
    @Override
    public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
        UUID id=UUID.fromString(idString);
        User user=userRepository.getUserById(id);
        return new org.springframework.security.core.userdetails.User(
                idString,user.getPassword(),user.getAuthorities()
        );
    }
}
