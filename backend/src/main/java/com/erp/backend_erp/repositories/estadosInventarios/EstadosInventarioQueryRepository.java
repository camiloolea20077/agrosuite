package com.erp.backend_erp.repositories.estadosInventarios;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstadosInventarioQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EstadosInventarioQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByCodigo(String codigo) {
        String sql = "SELECT COUNT(1) > 0 FROM estados_inventario WHERE LOWER(codigo) = :codigo";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("codigo", codigo);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    // public PageImpl<EstadosInventarioTableDto> listEstadosInventario(PageableDto<Object> pageableDto) {
    //     // Aquí implementarías la paginación usando tu utilitario de consultas
    //     return null; // TODO: Implementar según tu estructura de mapper
    // }
}
