package com.jp.dev.users.application.user;

import com.jp.dev.commons.annotations.WebAdapter;
import com.jp.dev.commons.exceptions.BadRequestException;
import com.jp.dev.users.application.user.request.UserRequest;
import com.jp.dev.users.application.user.response.UserResponse;
import com.jp.dev.users.domain.ports.in.UserCrudPort;
import com.jp.dev.users.domain.ports.in.UsernamesPort;
import com.jp.dev.users.domain.user.Role;
import com.jp.dev.users.domain.user.User;
import com.jp.dev.users.infrastructure.mapper.UserMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@WebAdapter
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  UsernamesPort usernamesPort;
  UserCrudPort userCrudPort;
  UserMapper userMapper;

  @Autowired
  public UserController(
      UsernamesPort usernamesPort, UserCrudPort userCrudPort, UserMapper userMapper) {
    this.usernamesPort = usernamesPort;
    this.userCrudPort = userCrudPort;
    this.userMapper = userMapper;
  }

  @PostMapping("/users")
  public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {

    log.info("creating user");

    if (!userRequest.getRole().equals(Role.ROLE_USER)) {
      log.warn("trying to create an admin");
      throw new BadRequestException("You are trying to create and admin");
    }

    User user = userCrudPort.createUser(userMapper.toUser(userRequest));

    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(user));
  }

  @GetMapping("/users")
  public List<User> getUsers() {
    return userCrudPort.getUsers();
  }

  @GetMapping("/users/{username}")
  public User getUser(@PathVariable(name = "username") String username) {
    return userCrudPort.getUser(username);
  }

  @PatchMapping("/users")
  public UserResponse updateUser(@RequestBody UserRequest userRequest) {
    User user = userCrudPort.updateUser(userMapper.toUser(userRequest));
    return userMapper.toUserResponse(user);
  }

  @GetMapping("/users/usernames")
  public List<String> getUsernamesByApp(@RequestParam String app) {
    return usernamesPort.getUsernamesByApp(app);
  }

  @PreAuthorize("hasRole('ROOT')")
  @PostMapping("/users/admin")
  public ResponseEntity<UserResponse> createUserAdmin(@RequestBody @Valid UserRequest userRequest) {

    log.info("creating admin");

    if (!userRequest.getRole().equals(Role.ROLE_ROOT)) {
      log.warn("trying to create a not admin user");
      throw new BadRequestException("You are not creating and admin");
    }

    User user = userCrudPort.createUser(userMapper.toUser(userRequest));

    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(user));
  }
}
