package com.erp.backend_erp.services.implementations;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.users.CreateUserDto;
import com.erp.backend_erp.dto.users.UserDto;
import com.erp.backend_erp.dto.users.UsersTableDto;
import com.erp.backend_erp.entity.UserEntity;
import com.erp.backend_erp.mappers.users.UserMappers;
import com.erp.backend_erp.repositories.users.UserJPARepository;
import com.erp.backend_erp.repositories.users.UserQueryRepository;
import com.erp.backend_erp.services.UserService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class UserServiceImpl implements UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserJPARepository userJPARepository;
    private final UserMappers userMappers;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserQueryRepository userQueryRepository, UserJPARepository userJPARepository,
            UserMappers userMappers, PasswordEncoder passwordEncoder) {
        this.userQueryRepository = userQueryRepository;
        this.userJPARepository = userJPARepository;
        this.userMappers = userMappers;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        Boolean exists = userQueryRepository.existsByEmail(createUserDto.getEmail().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El correo ya se encuentra registrado");

        try {
            String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());
            createUserDto.setPassword(encodedPassword);
            UserEntity userEntity = userMappers
                    .createToEntity(createUserDto);
            System.out.println("Guardando usuario: " + userEntity.toString());
            UserEntity saveUserEntity = userJPARepository
                    .save(userEntity);

            return userMappers.toDto(saveUserEntity);
        } catch (Exception e) {
            System.err.println("Error al crear el usuario: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }
    @Override
    public PageImpl<UsersTableDto> pageUsers(PageableDto<Object> pageableDto) {
        return userQueryRepository.listUsers(pageableDto);
    }
}
