package com.connect.connectingpeople.security;

import com.connect.connectingpeople.service.UsersService;
import com.connect.connectingpeople.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final Environment environment;
    private final JwtTokenUtil jwtTokenUtil;
    private final UsersService usersService;

    public JwtTokenVerifier(Environment environment, JwtTokenUtil jwtTokenUtil, UsersService usersService) {
        this.environment = environment;
        this.jwtTokenUtil = jwtTokenUtil;
        this.usersService = usersService;
    }

    @Override
    @Deprecated
    protected void doFilterInternal(HttpServletRequest req,
                                    @NonNull HttpServletResponse res,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isEmpty() ||
                !authorizationHeader.startsWith(requireNonNull(environment.getProperty("token.prefix")))) {
            filterChain.doFilter(req, res);
            return;
        }
        String token = authorizationHeader.replace(requireNonNull(environment.getProperty("token.prefix")), "");

        try {
            Claims body = jwtTokenUtil.getAllClaimsFromToken(token);

            String username = body.getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                var authorities = (List<Map<String, String>>) body.get("authorities");

                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());

                final UserDetails userDetails =
                        usersService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            simpleGrantedAuthorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }


        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }
        filterChain.doFilter(req, res);

    }
}
