package com.jp.dev.users.infrastructure.security;

import com.jp.dev.users.infrastructure.entity.UserEntity;
import com.jp.dev.users.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(
      String username) throws UsernameNotFoundException {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new CustomUserDetails(userEntity);
  }
}
