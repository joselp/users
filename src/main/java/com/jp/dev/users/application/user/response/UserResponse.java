package com.jp.dev.users.application.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private String app;
}
