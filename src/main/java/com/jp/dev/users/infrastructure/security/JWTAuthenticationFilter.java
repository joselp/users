package com.jp.dev.users.infrastructure.security;

import static com.jp.dev.commons.security.SecurityConstants.EXPIRATION_TIME;
import static com.jp.dev.commons.security.SecurityConstants.SECRET;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.dev.commons.exceptions.NotFoundException;
import com.jp.dev.users.controller.user.request.UserRequest;
import com.jp.dev.users.domain.token.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;

    setFilterProcessesUrl("/authenticate");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    try {
      UserRequest user = new ObjectMapper().readValue(req.getInputStream(), UserRequest.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              user.getUsername(), user.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
      throws IOException {

    CustomUserDetails loggedUser = (CustomUserDetails) auth.getPrincipal();
    String token =
        JWT.create()
            .withSubject(loggedUser.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .withClaim(
                "role",
                loggedUser
                    .getAuthorities()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("No role found"))
                    .getAuthority())
            .sign(Algorithm.HMAC512(SECRET.getBytes()));

    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
    res.getWriter().write(new ObjectMapper().writeValueAsString(new Token(token)));
    res.getWriter().flush();

    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);

    System.out.println("------ " + failed.getMessage());
  }
}
