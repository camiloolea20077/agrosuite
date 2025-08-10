package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.users.CreateUserDto;
import com.erp.backend_erp.dto.users.UserDto;
import com.erp.backend_erp.dto.users.UsersTableDto;
import com.erp.backend_erp.util.PageableDto;

public interface UserService {
    UserDto create(CreateUserDto createUserDto);
    PageImpl<UsersTableDto> pageUsers(PageableDto<Object> pageableDto);
    
}
