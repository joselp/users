package com.jp.dev.users.domain.usecase;

import com.jp.dev.commons.annotations.UseCase;
import com.jp.dev.users.domain.ports.in.UserCrudPort;
import com.jp.dev.users.domain.ports.out.UserCrudData;
import com.jp.dev.users.domain.user.User;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UseCase
public class UserCrudUseCase implements UserCrudPort {

  private UserCrudData userCrudData;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserCrudUseCase(UserCrudData userCrudData, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userCrudData = userCrudData;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public User createUser(User user) {
    // All business logic if it's necessary
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    return userCrudData.save(user);
  }

  @Override
  public User getUser(String username) {
    return userCrudData.getByUsername(username);
  }

  @Override
  public User updateUser(User user) {

    if (Objects.nonNull(user.getPassword())) {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }
    return userCrudData.update(user);
  }

  @Override
  public List<User> getUsers() {
    // All business logic if it's necessary
    return userCrudData.findAllUsers();
  }
}
