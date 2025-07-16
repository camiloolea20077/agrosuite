package com.erp.backend_erp.repositories.role;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.erp.backend_erp.entity.role.Role;

@Repository
public class RoleQueryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

        public Optional<Role> findByName(String name)  {
        String sql = "SELECT id, name, description, created_at, updated_at, deleted_at FROM roles WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
    
        List<Role> roles = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Role.class));
        return roles.stream().findFirst();
    }
    public Boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM roles WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
}
