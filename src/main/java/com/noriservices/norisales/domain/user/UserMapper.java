package com.noriservices.norisales.domain.user;

import com.noriservices.norisales.domain.user.dto.ResponseUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserDTO toResponse(UserModel entity);
}
