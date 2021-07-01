package com.connect.connectingpeople.security;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.model.UserLogin;
import com.connect.connectingpeople.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static java.util.Objects.requireNonNull;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersService userService;
    private final Environment environment;
    private final SecretKey secretKey;

    public AuthenticationFilter(UsersService userService,
                                Environment environment,
                                AuthenticationManager authenticationManager,
                                SecretKey secretKey) {
        super();
        this.userService = userService;
        this.environment = environment;
        this.secretKey = secretKey;
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        try{
            UserLogin creds = mapper.readValue(request.getInputStream(), UserLogin.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @Deprecated
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        String username = ((User) authResult.getPrincipal()).getUsername();
        System.out.println(username);
        UserDto userDetails = userService.findUserByUsername(username);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(requireNonNull(environment.getProperty("token.expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        response.addHeader(HttpHeaders.AUTHORIZATION, environment.getProperty("token.prefix") + token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
