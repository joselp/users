package com.jp.dev.users.domain.ports.in;

import com.jp.dev.users.domain.user.User;
import java.util.List;

public interface UserCrudPort {

  User createUser(User user);

  User getUser(String username);

  User updateUser(User user);

  List<User> getUsers();
}
