package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.users.CreateUserDto;
import com.erp.backend_erp.dto.users.UserDto;

public interface UserService {
    UserDto create(CreateUserDto createUserDto);
}
