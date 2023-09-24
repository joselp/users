package com.jp.dev.users.infrastructure.adapter;

import com.jp.dev.commons.annotations.PersistenceAdapter;
import com.jp.dev.commons.exceptions.NotFoundException;
import com.jp.dev.users.domain.ports.out.UserCrudData;
import com.jp.dev.users.domain.user.User;
import com.jp.dev.users.infrastructure.entity.UserEntity;
import com.jp.dev.users.infrastructure.mapper.UserMapper;
import com.jp.dev.users.infrastructure.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

@PersistenceAdapter
public class UserCrudAdapter implements UserCrudData {

  private UserMapper userMapper;
  private UserRepository userRepository;

  @Autowired
  public UserCrudAdapter(UserMapper userMapper, UserRepository userRepository) {
    this.userMapper = userMapper;
    this.userRepository = userRepository;
  }

  @Override
  public User save(User user) {

    UserEntity entity = userRepository.save(userMapper.toUserEntity(user));

    return userMapper.toUser(entity);
  }

  @Override
  public User getByUsername(String username) {
    UserEntity entity =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found"));
    return userMapper.toUser(entity);
  }

  @Override
  public User update(User user) {

    UserEntity entity =
        userRepository
            .findByUsername(user.getUsername())
            .orElseThrow(() -> new NotFoundException("User not found"));

    if (Objects.nonNull(user.getEmail())) {
      entity.setEmail(user.getEmail());
    }
    if (Objects.nonNull(user.getPassword())) {
      entity.setPassword(user.getPassword());
    }
    if (Objects.nonNull(user.getFirstName())) {
      entity.setFirstName(user.getFirstName());
    }
    if (Objects.nonNull(user.getLastName())) {
      entity.setLastName(user.getLastName());
    }
    return userMapper.toUser(userRepository.save(entity));
  }

  @Override
  public List<User> findAllUsers() {

    return userMapper.toListOfUser(userRepository.findAll());
  }
}
