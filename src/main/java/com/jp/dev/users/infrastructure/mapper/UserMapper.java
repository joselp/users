package com.jp.dev.users.infrastructure.mapper;

import com.jp.dev.users.application.user.request.UserRequest;
import com.jp.dev.users.application.user.response.UserResponse;
import com.jp.dev.users.domain.user.User;
import com.jp.dev.users.infrastructure.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(UserRequest dto);

  User toUser(UserEntity userEntity);

  @Mapping(target = "id", ignore = true)
  UserEntity toUserEntity(User user);

  @Mapping(target = "id", ignore = true)
  List<User> toListOfUser(List<UserEntity> entities);

  UserResponse toUserResponse(User user);
}
