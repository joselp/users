package com.jp.dev.users.infrastructure.security;

import com.jp.dev.users.domain.user.Role;
import com.jp.dev.users.infrastructure.entity.UserEntity;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetailsTest {

  private UserEntity userEntity;
  private CustomUserDetails userDetails;

  @BeforeEach
  public void setup() {
    userEntity = UserEntity.builder()
        .username("username")
        .password("password")
        .role(Role.ROLE_USER)
        .build();
    userDetails = new CustomUserDetails(userEntity);
  }

  @Test
  public void testGetUsername() {
    // Verify that the getUsername() method returns the correct username
    Assertions.assertEquals("username", userDetails.getUsername());
  }

  @Test
  public void testGetPassword() {
    // Verify that the getPassword() method returns the correct password
    Assertions.assertEquals("password", userDetails.getPassword());
  }

  @Test
  public void testGetAuthorities() {
    // Verify that the getAuthorities() method returns the correct authorities
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
    Assertions.assertEquals(1, authorities.size());
    Assertions.assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.ROLE_USER.name())));
  }

  @Test
  public void testIsAccountNonExpired() {
    // Verify that the isAccountNonExpired() method returns true
    Assertions.assertTrue(userDetails.isAccountNonExpired());
  }

  @Test
  public void testIsAccountNonLocked() {
    // Verify that the isAccountNonLocked() method returns true
    Assertions.assertTrue(userDetails.isAccountNonLocked());
  }

  @Test
  public void testIsCredentialsNonExpired() {
    // Verify that the isCredentialsNonExpired() method returns true
    Assertions.assertTrue(userDetails.isCredentialsNonExpired());
  }

  @Test
  public void testIsEnabled() {
    // Verify that the isEnabled() method returns true
    Assertions.assertTrue(userDetails.isEnabled());
  }

  @Test
  public void testGetUser() {
    // Verify that the getUser() method returns the correct UserEntity object
    Assertions.assertEquals(userEntity, userDetails.getUser());
  }
}
