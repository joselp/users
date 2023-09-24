package com.jp.dev.users.domain.ports.in;

import java.util.List;

public interface UsernamesPort {

  List<String> getUsernamesByApp(String app);
}
