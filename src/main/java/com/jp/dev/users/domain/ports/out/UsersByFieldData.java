package com.jp.dev.users.domain.ports.out;

import com.jp.dev.users.domain.user.User;
import java.util.List;

public interface UsersByFieldData {

  List<User> getUsersByApp(String app);
}
