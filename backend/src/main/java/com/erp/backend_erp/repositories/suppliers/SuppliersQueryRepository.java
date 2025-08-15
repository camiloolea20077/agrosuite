package com.erp.backend_erp.repositories.suppliers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SuppliersQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SuppliersQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByCodigo(String codigo) {
        String sql = "SELECT COUNT(1) > 0 FROM suppliers WHERE LOWER(codigo) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigo);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    // public PageImpl<SuppliersTableDto> listSuppliers(PageableDto<Object> pageableDto) {
    //     return null; // Implementar según tu estructura de mappers
    // }
}