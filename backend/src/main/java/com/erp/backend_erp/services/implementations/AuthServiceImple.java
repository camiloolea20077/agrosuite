package com.erp.backend_erp.services.implementations;

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
import com.erp.backend_erp.dto.auth.UserDetailDto;
import com.erp.backend_erp.mappers.users.UserMappers;
import com.erp.backend_erp.repositories.auth.AuthQueryRepository;
import com.erp.backend_erp.repositories.farms.FarmsJPARepository;
import com.erp.backend_erp.repositories.farms.FarmsQueryRepository;
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

    private final UserQueryRepository userQueryRepository;
    private final FarmsQueryRepository farmsQueryRepository;
    private final FarmsJPARepository farmsJPARepository;
    private final UserJPARepository userJPARepository;
    private final UserMappers userMappers;
    private final RoleQueryRepository roleQueryRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImple(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider,
            UserQueryRepository userQueryRepository, UserJPARepository userJPARepository, UserMappers userMappers, PasswordEncoder passwordEncoder,
            RoleQueryRepository roleQueryRepository,
            FarmsJPARepository farmsJPARepository,
            FarmsQueryRepository farmsQueryRepository
            ) {
        _jwtTokenProvider = jwtTokenProvider;
        this.userQueryRepository = userQueryRepository;
        this.userJPARepository = userJPARepository;
        this.userMappers = userMappers;
        this.farmsQueryRepository = farmsQueryRepository;
        this.farmsJPARepository = farmsJPARepository;
        this.roleQueryRepository = roleQueryRepository;
        this.passwordEncoder = passwordEncoder;
        _authenticationManager = authenticationManager;
        _userService = userService;
    }

    public AuthDto login(LoginDto loginDto) {
        try {
            Authentication authentication = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Obtener el usuario
            UserDetailDto user = authQueryRepository.findByUserLogin(loginDto.getEmail());
            if (user == null) {
                throw new GlobalException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
                
            // Verificar si el rol es SUPER_ADMIN
            if (!user.getRole().equals("SUPER_ADMIN")) {
                // Si el usuario no es SUPER_ADMIN, mandamos un mensaje indicando que debe seleccionar una finca
                if (loginDto.getFarmId() == null) {
                    throw new GlobalException(HttpStatus.FORBIDDEN, "No es Super Admin, elija una finca para logear");
                }
                // Solo si el rol no es SUPER_ADMIN, validamos la finca
                Long farmId = loginDto.getFarmId();
                
                // Verificar si el campo 'farm' del usuario es null
                if (user.getFarm() == null) {
                    throw new GlobalException(HttpStatus.FORBIDDEN, "El usuario no tiene una finca asignada");
                }
                
                // Verificar si el usuario tiene acceso a la finca solicitada
                boolean userHasAccess = user.getFarm().equals(farmId);
                if (!userHasAccess) {
                    throw new GlobalException(HttpStatus.FORBIDDEN, "El usuario no tiene acceso a esta finca");
                }
            }
            // Generación del token
            String token = _jwtTokenProvider.generateToken(authentication, loginDto.getEmail());
            
            // Crear DTO de respuesta
            AuthDto authDto = new AuthDto();
            authDto.setUser(user);
            authDto.setToken(token);
            
            return authDto;
        } catch (BadCredentialsException e) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


// @Override
// @Transactional
// public AuthDto register(RegisterRequestDto dto) {
    
//     if (dto.getFarm_id() == null) {
//         throw new GlobalException(HttpStatus.BAD_REQUEST, "El ID de finca es requerido");
//     }
//     try {
//         Role role = roleQueryRepository.findByName("ADMIN")
//                 .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "Rol ADMIN no encontrado"));

//         FarmEntity farm = farmsJPARepository.findById(dto.getFarm_id())
//                 .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "Finca no encontrada"));
//         UserEntity userEntity = userMappers.createToEntity(userDto);
//         userEntity.setUsername(dto.getAdmin_email());
//         userEntity.setEmail(dto.getAdmin_email());
//         userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
//         userEntity.setActivo(1L);
//         userEntity.setCreated_at(LocalDateTime.now());
//         userEntity.setUpdated_at(LocalDateTime.now());

//         // Guardar solo una vez
//         userJPARepository.save(userEntity);

//         Authentication auth = new UsernamePasswordAuthenticationToken(dto.getAdmin_email(), dto.getPassword());
//         String token = _jwtTokenProvider.generateToken(auth, dto.getAdmin_email());

//         UserDetailDto userDetail = authQueryRepository.findByUserLogin(dto.getAdmin_email());
//         AuthDto authDto = new AuthDto();
//         authDto.setUser(userDetail);
//         authDto.setToken(token);

//         return authDto;

//     } catch (Exception e) {
//         System.err.println("Error en el registro: " + e.getMessage());
//         throw new GlobalException(HttpStatus.CONFLICT, "Error al registrar el usuario: " + e.getMessage());
//     }
// }



    

}
