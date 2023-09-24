package com.jp.dev.users.application.user.request;

import com.jp.dev.users.domain.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  @NotBlank private String email;
  @NotBlank private String username;
  @NotBlank private String password;
  private String firstName;
  private String lastName;
  @NotBlank private String app;
  @NotNull private Role role;

  private Map<String, String> properties;
}
