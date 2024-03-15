package com.bgod.auth.Security;

import com.bgod.auth.Service.UserDetailService;
import com.bgod.auth.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtService extends OncePerRequestFilter {
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private UserDetailService userService;


    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token;
        String headerId;
        String headerPassword;
        String headerRole;

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }



        try {// tokendan userı çekiyoruz
            token =authHeader.substring(7);
            headerId = jwtToken.extractUserId(token);
            headerPassword = userService.getPasswordAndRole(token,true);
            headerRole = userService.getPasswordAndRole(token,false);

        } catch (Exception e) {

            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = new User(
                    headerId,
                    headerPassword,
                    Set.of(new SimpleGrantedAuthority(headerRole))
            );

            if (jwtToken.isTokenValid(token, user)) {//Geçerli bir JWT varsa ve kullanıcı doğrulaması başarılıysa securityde kullanıcı doğrulama için kullanılır
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                //güvenli bağlanması için oluşturulan getcontext kullanıcı kimliği
            }
        }
        filterChain.doFilter(request, response);
    }

}
