package com.jp.dev.users.domain.user;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  private String email;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String app;
  private Role role;
  private Map<String, String> properties;
}
