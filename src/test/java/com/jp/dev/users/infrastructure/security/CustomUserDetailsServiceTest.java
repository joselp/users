package com.jp.dev.users.infrastructure.security;

import com.jp.dev.users.infrastructure.entity.UserEntity;
import com.jp.dev.users.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  private CustomUserDetailsService userDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userDetailsService = new CustomUserDetailsService(userRepository);
  }

  @Test
  void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
    // Arrange
    String username = "john";
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

    // Act
    org.springframework.security.core.userdetails.UserDetails userDetails =
        userDetailsService.loadUserByUsername(username);

    // Assert
    assertNotNull(userDetails);
    assertEquals(username, userDetails.getUsername());
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  void loadUserByUsername_NonExistingUser_ThrowsUsernameNotFoundException() {
    // Arrange
    String username = "nonexistent";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername(username));
    verify(userRepository, times(1)).findByUsername(username);
  }
}