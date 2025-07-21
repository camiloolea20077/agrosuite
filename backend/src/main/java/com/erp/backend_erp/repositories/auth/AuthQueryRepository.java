package com.erp.backend_erp.repositories.auth;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.auth.UserDetailDto;
import com.erp.backend_erp.util.MapperRepository;

@Service
public class AuthQueryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Lazy
    private MapperRepository mapperRepository;

    public UserDetailDto findByUserLogin(String email) {
        try {
            String sql = """
            SELECT 
                u.id AS id,
                u.email AS email,
                u.nombre_completo AS name,
                r.nombre AS role,
                f.id as farm,
                f.nombre as farm_name,
                string_to_array(array_to_string(u.permisos, ','), ',') as permisos
            FROM public.users u
            LEFT JOIN public.roles r ON r.id = u.role_id
            LEFT JOIN public.farms f on f.id = u.farm_id
            WHERE u.deleted_at IS NULL AND u.email = ?
            LIMIT 1;
            """;
            // Ejecutar la consulta SQL
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, email);
            Object rawPermisos = result.get("permisos");
            if (rawPermisos instanceof java.sql.Array arraySql) {
                result.put("permisos", List.of((String[]) arraySql.getArray()));
            } else if (rawPermisos instanceof String str) {
                result.put("permisos", List.of(str.split(",")));
            } else if (rawPermisos == null) {
                result.put("permisos", List.of());
            }
            // Convertir el resultado a DTO
            return mapperRepository.mapToDto(result, UserDetailDto.class);

        } catch (EmptyResultDataAccessException e) {
            return null; // Devolver null si no hay resultados
        } catch (Exception e) {
            // Manejar otros errores de SQL o conversión
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la consulta", e);
        }
    }
}
