package com.jp.dev.users.domain.usecase;

import com.jp.dev.commons.annotations.UseCase;
import com.jp.dev.users.domain.ports.in.UsernamesPort;
import com.jp.dev.users.domain.ports.out.UsersByFieldData;
import com.jp.dev.users.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@UseCase
public class UsersByFieldUseCase implements UsernamesPort {

  private UsersByFieldData usersByFieldData;

  @Autowired
  public UsersByFieldUseCase(UsersByFieldData usersByFieldData) {
    this.usersByFieldData = usersByFieldData;
  }

  @Override
  public List<String> getUsernamesByApp(String app) {
    return usersByFieldData
        .getUsersByApp(app)
        .stream()
        .map(User::getUsername)
        .collect(Collectors.toList());
  }
}
