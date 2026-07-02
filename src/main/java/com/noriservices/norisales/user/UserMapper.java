package com.noriservices.norisales.user;

import com.noriservices.norisales.user.dto.ResponseUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserDTO toResponse(User entity);
}
