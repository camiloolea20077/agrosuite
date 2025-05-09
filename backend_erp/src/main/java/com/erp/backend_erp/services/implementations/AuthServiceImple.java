package com.erp.backend_erp.services.implementations;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.auth.AuthDto;
import com.erp.backend_erp.dto.auth.LoginDto;
import com.erp.backend_erp.dto.auth.RegisterRequestDto;
import com.erp.backend_erp.dto.auth.UserDetailDto;
import com.erp.backend_erp.dto.companies.CreateCompaniesDto;
import com.erp.backend_erp.dto.users.CreateUserDto;
import com.erp.backend_erp.entity.UserEntity;
import com.erp.backend_erp.entity.companies.CompaniesEntity;
import com.erp.backend_erp.entity.role.Role;
import com.erp.backend_erp.mappers.company.CompanyMapper;
import com.erp.backend_erp.mappers.register.RegisterMapper;
import com.erp.backend_erp.mappers.users.UserMappers;
import com.erp.backend_erp.repositories.auth.AuthQueryRepository;
import com.erp.backend_erp.repositories.companies.CompaniesJPARepository;
import com.erp.backend_erp.repositories.companies.CompaniesQueryRepository;
import com.erp.backend_erp.repositories.role.RoleQueryRepository;
import com.erp.backend_erp.repositories.users.UserJPARepository;
import com.erp.backend_erp.repositories.users.UserQueryRepository;
import com.erp.backend_erp.security.JwtTokenProvider;
import com.erp.backend_erp.services.AuthService;
import com.erp.backend_erp.services.UserService;
import com.erp.backend_erp.util.AESencryptUtil;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.MapperRepository;

@Service
public class AuthServiceImple implements AuthService {

    private static final Integer TIME_TOKEN = 5;

    private static final String SEPARATOR = "---";

    @Autowired
    private AESencryptUtil encrypt;

    @Autowired
    private AuthQueryRepository authQueryRepository;

    @Autowired
    private MapperRepository mapperRepository;

    @Autowired
    private JwtTokenProvider _jwtTokenProvider;

    private final AuthenticationManager _authenticationManager;

    private final UserService _userService;

    private final CompaniesQueryRepository companiesQueryRepository;
    private final CompaniesJPARepository companiesJPARepository;
    private final CompanyMapper companyMapper;
    private final UserQueryRepository userQueryRepository;
    private final UserJPARepository userJPARepository;
    private final UserMappers userMappers;
    private final RoleQueryRepository roleQueryRepository;
    private final RegisterMapper registerMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImple(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider,
            CompaniesQueryRepository companiesQueryRepository, CompaniesJPARepository companiesJPARepository, CompanyMapper companyMapper,
            UserQueryRepository userQueryRepository, UserJPARepository userJPARepository, UserMappers userMappers, PasswordEncoder passwordEncoder,
            RegisterMapper registerMapper, RoleQueryRepository roleQueryRepository) {
        _jwtTokenProvider = jwtTokenProvider;
        this.userQueryRepository = userQueryRepository;
        this.userJPARepository = userJPARepository;
        this.userMappers = userMappers;
        this.roleQueryRepository = roleQueryRepository;
        this.registerMapper = registerMapper;
        this.passwordEncoder = passwordEncoder;
        this.companiesQueryRepository = companiesQueryRepository;
        this.companiesJPARepository = companiesJPARepository;
        this.companyMapper = companyMapper;
        _authenticationManager = authenticationManager;
        _userService = userService;
    }

    @Override
    public AuthDto login(LoginDto loginDto) {
        try {
            System.out.println("Iniciando el proceso de autenticación para: " + loginDto.getEmail());

            Authentication authentication = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Generación del token
            String token = _jwtTokenProvider.generateToken(authentication, loginDto.getEmail());
            // Obtener el usuario
            UserDetailDto user = authQueryRepository.findByUserLogin(loginDto.getEmail());
            if (user == null) {
                throw new GlobalException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            // Crear DTO de respuesta
            AuthDto authDto = new AuthDto();
            authDto.setUser(user);
            authDto.setToken(token);
            // Log de salida
            System.out.println("Autenticación exitosa para: " + loginDto.getEmail());
            return authDto;
        } catch (BadCredentialsException e) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    public AuthDto register(RegisterRequestDto dto) {
        // Validar que el NIT no esté repetido
        Boolean exists = companiesQueryRepository.existsByNit(dto.getNit().toLowerCase());
        if (exists) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El NIT ya está registrado");
        }
        try {
            // Crear y guardar empresa
            CreateCompaniesDto createCompanyDto = registerMapper.toCreateCompanyDto(dto);
            CompaniesEntity companyEntity = companyMapper.createToEntity(createCompanyDto);
            CompaniesEntity savedCompany = companiesJPARepository.save(companyEntity);
            // Buscar rol ADMIN
            Role role = roleQueryRepository.findByName("ADMIN")
            .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "Rol ADMIN no encontrado"));
            CreateUserDto userDto = registerMapper.toCreateUserDto(dto);
            userDto.setCompany_id(savedCompany.getId());
            UserEntity userEntity = userMappers.createToEntity(userDto);
            userEntity.setRole(role);
            userEntity.setUsername(dto.getAdmin_email());
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setCreated_at(LocalDateTime.now());
            userEntity.setUpdated_at(LocalDateTime.now());
            userJPARepository.save(userEntity);
            // Generar token
            Authentication auth = new UsernamePasswordAuthenticationToken(dto.getAdmin_email(), dto.getPassword());
            String token = _jwtTokenProvider.generateToken(auth, dto.getAdmin_email());
            // Obtener detalles del usuario
            UserDetailDto userDetail = authQueryRepository.findByUserLogin(dto.getAdmin_email());
            // Armar respuesta
            AuthDto authDto = new AuthDto();
            authDto.setUser(userDetail);
            authDto.setToken(token);
            return authDto;
    
        } catch (Exception e) {
            System.err.println("Error en el registro: " + e.getMessage());
            throw new RuntimeException("Error al registrar empresa y usuario: " + e.getMessage(), e);
        }
    }
    

}
