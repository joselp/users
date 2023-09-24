package com.jp.dev.users.domain.ports.out;

import com.jp.dev.users.domain.user.User;
import java.util.List;

public interface UserCrudData {

  User save(User user);

  User getByUsername(String username);

  User update(User user);

  List<User> findAllUsers();
}
