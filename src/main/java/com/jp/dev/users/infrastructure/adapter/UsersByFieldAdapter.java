package com.jp.dev.users.infrastructure.adapter;

import com.jp.dev.commons.annotations.PersistenceAdapter;
import com.jp.dev.users.domain.ports.out.UsersByFieldData;
import com.jp.dev.users.domain.user.User;
import com.jp.dev.users.infrastructure.mapper.UserMapper;
import com.jp.dev.users.infrastructure.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@PersistenceAdapter
public class UsersByFieldAdapter implements UsersByFieldData {

  private UserMapper userMapper;
  private UserRepository userRepository;

  @Autowired
  public UsersByFieldAdapter(UserMapper userMapper, UserRepository userRepository) {
    this.userMapper = userMapper;
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getUsersByApp(String app) {
    return userMapper.toListOfUser(userRepository.findAllByApp(app));
  }
}
