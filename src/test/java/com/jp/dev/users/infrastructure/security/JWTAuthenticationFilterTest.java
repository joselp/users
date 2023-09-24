package com.jp.dev.users.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.dev.users.application.user.request.UserRequest;
import com.jp.dev.users.domain.user.Role;
import com.jp.dev.users.infrastructure.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationFilterTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  private JWTAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager);
  }

  @Test
  public void attemptAuthentication_ValidUserRequest_ReturnsAuthentication() throws IOException {
    // Arrange
    UserRequest userRequest =
        UserRequest.builder().username("username").password("password")
            .role(Role.ROLE_USER)
            .build();
    InputStream inputStream =
        new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(userRequest));
    ServletInputStream servletInputStream=new ServletInputStream(){
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener listener) {

      }

      public int read() throws IOException {
        return inputStream.read();
      }
    };
    when(request.getInputStream()).thenReturn(servletInputStream);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(mock(Authentication.class));

    // Act
    Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

    // Assert
    assertNotNull(result);
    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  public void successfulAuthentication_Authentication_SuccessfulResponse() throws IOException {
    // Arrange

    CustomUserDetails userDetails =
        new CustomUserDetails(
            UserEntity.builder()
                .username("username")
                .password("password")
                .role(Role.ROLE_USER)
                .properties(new HashMap<>())
                .build());
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authentication.getAuthorities()).thenReturn(new ArrayList<>());
    when(response.getWriter()).thenReturn(mock(PrintWriter.class));

    // Act
    jwtAuthenticationFilter.successfulAuthentication(
        request, response, filterChain, authentication);

    // Assert
    verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    verify(response.getWriter()).write(anyString());
    verify(response.getWriter()).flush();
  }

  @Test
  public void unsuccessfulAuthentication_Exception_PrintsErrorMessage()
      throws IOException, ServletException {
    // Arrange
    AuthenticationException exception = new AuthenticationException("Invalid credentials") {};
    when(response.getWriter()).thenReturn(mock(PrintWriter.class));

    // Act
    jwtAuthenticationFilter.unsuccessfulAuthentication(request, response, exception);
  }
}
